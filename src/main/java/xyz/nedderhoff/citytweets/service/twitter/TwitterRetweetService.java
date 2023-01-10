package xyz.nedderhoff.citytweets.service.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.api.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.RetweetEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.cache.twitter.RetweetCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.service.AbstractRepostService;

import java.util.function.Consumer;

@Service
public class TwitterRetweetService extends AbstractRepostService<Long, TwitterAccount, RetweetCache, TwitterAccountService> {
    private static final Logger logger = LoggerFactory.getLogger(TwitterRetweetService.class);

    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final RetweetEndpoint retweetEndpoint;
    private final MeEndpoint meEndpoint;

    public TwitterRetweetService(
            TwitterAccountService accountService,
            RecentTweetsEndpoint recentTweetsEndpoint,
            RetweetEndpoint retweetEndpoint,
            MeEndpoint meEndpoint,
            RetweetCache retweetCache
    ) {
        super(retweetCache, accountService);
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.retweetEndpoint = retweetEndpoint;
        this.meEndpoint = meEndpoint;
    }

    @Override
    public void repost() {
        if (accountService.getAccounts() == null) {
            logger.info("No Twitter accounts configured - skipping ...");
        }

        retweet();
    }

    private void retweet() {
        accountService.getAccounts().forEach(account -> {
            logger.info("Looking for unseen tweets for search {} on Twitter account {}", account.search(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.search()).stream()
                        .filter(tweet -> shouldRetweet(tweet, myId, account))
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

    private boolean shouldRetweet(Tweet tweet, long myId, TwitterAccount account) {
        final Consumer<Long> hasBeenSeenLogger = (id) ->
                logger.warn("Tweet {} from user {} was already reposted:\n{}", id, tweet.user().username(), tweet.text());
        final Consumer<String> authorBlockedLogger = (username) ->
                logger.warn("Tweet {} from user {} can't be reposted as author is blocked:\n{}", tweet.id(), username, tweet.text());

        return !isFromMe(tweet, myId)
                && !isRetweet(tweet)
                && !hasBeenSeen(tweet.id(), hasBeenSeenLogger)
                && !isAuthorBlocked(tweet.user().username(), account, authorBlockedLogger);
    }

    protected boolean isFromMe(Tweet tweet, long myUserId) {
        final boolean isFromMe = tweet.user().id() == myUserId;
        if (isFromMe) {
            logger.warn("Tweet {} from user {} is from me:\n{}", tweet.id(), tweet.user().username(), tweet.text());
        }
        return isFromMe;
    }

    private boolean isRetweet(Tweet tweet) {
        final boolean isRetweet = tweet.text().startsWith("RT @");
        if (isRetweet) {
            logger.warn("Tweet {} from user {} is retweet:\n{}", tweet.id(), tweet.user().username(), tweet.text());
        }
        return isRetweet;
    }
}
