package xyz.nedderhoff.citytweets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.api.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.RetweetEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.cache.twitter.RetweetCache;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.service.AccountService;
import xyz.nedderhoff.citytweets.service.RepostService;

@Component
public class TwitterRetweetService implements RepostService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterRetweetService.class);

    private final AccountService accountService;

    // Twitter Dependencies
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final RetweetEndpoint retweetEndpoint;
    private final MeEndpoint meEndpoint;
    private final RetweetCache retweetCache;

    public TwitterRetweetService(
            AccountService accountService,
            RecentTweetsEndpoint recentTweetsEndpoint,
            RetweetEndpoint retweetEndpoint,
            MeEndpoint meEndpoint,
            RetweetCache retweetCache
    ) {
        this.accountService = accountService;
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.retweetEndpoint = retweetEndpoint;
        this.meEndpoint = meEndpoint;
        this.retweetCache = retweetCache;
    }

    @Override
    public void run() {
        accountService.getTwitterAccounts().forEach(account -> {
            logger.info("Looking for unseen tweets for search {} on Twitter account {}", account.search(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.search()).stream()
                        .filter(tweet -> shouldRetweet(tweet, myId))
                        .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                                tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                        )
                        .forEach(t -> retweetEndpoint.retweet(t, account));
            } catch (TwitterException e) {
                logger.error("Exception during retweet job", e);
            }
        });
    }

    private boolean shouldRetweet(Tweet tweet, long myId) {
        return !isTweetFromMe(tweet, myId)
                && !isRetweet(tweet)
                && !hasBeenSeen(tweet);
    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isRetweet(Tweet tweet) {
        return tweet.text().startsWith("RT @");
    }

    private boolean hasBeenSeen(Tweet tweet) {
        return retweetCache.contains(tweet.id());
    }
}
