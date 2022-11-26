package xyz.nedderhoff.citytweets.service.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.api.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.RetweetEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.cache.twitter.RetweetCache;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.service.AbstractRepostService;
import xyz.nedderhoff.citytweets.service.RepostService;

@Component
public class TwitterRetweetService extends AbstractRepostService<Long, RetweetCache> implements RepostService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterRetweetService.class);

    private final TwitterAccountService twitterAccountService;
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final RetweetEndpoint retweetEndpoint;
    private final MeEndpoint meEndpoint;

    public TwitterRetweetService(
            TwitterAccountService twitterAccountService,
            RecentTweetsEndpoint recentTweetsEndpoint,
            RetweetEndpoint retweetEndpoint,
            MeEndpoint meEndpoint,
            RetweetCache retweetCache
    ) {
        super(retweetCache);
        this.twitterAccountService = twitterAccountService;
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.retweetEndpoint = retweetEndpoint;
        this.meEndpoint = meEndpoint;
    }

    @Override
    public void repost() {
        if (twitterAccountService.getAccounts() == null) {
            logger.info("No Twitter accounts configured - skipping ...");
        }

        retweet();
    }

    private void retweet() {
        twitterAccountService.getAccounts().forEach(account -> {
            logger.info("Looking for unseen tweets for search {} on Twitter account {}", account.search(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.search()).stream()
                        .filter(tweet -> shouldRetweet(tweet, myId))
                        .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                                tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                        )
                        .map(tweet -> retweetEndpoint.retweet(tweet, account))
                        .forEach(tweet -> cache(tweet.id()));
            } catch (TwitterException e) {
                logger.error("Exception during retweet job", e);
            }
        });
    }

    private boolean shouldRetweet(Tweet tweet, long myId) {
        return !isFromMe(tweet, myId)
                && !isRetweet(tweet)
                && !hasBeenSeen(tweet.id());
    }

    protected boolean isFromMe(Tweet postUserId, long myUserId) {
        return postUserId.user().id() == myUserId;
    }

    private boolean isRetweet(Tweet tweet) {
        return tweet.text().startsWith("RT @");
    }
}
