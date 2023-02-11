package xyz.nedderhoff.citytweets.service.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.api.twitter.api1.FollowEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.cache.twitter.TwitterFollowerCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.service.AbstractFollowService;

@Service
public class TwitterFollowService extends AbstractFollowService<TwitterAccount, TwitterAccountService> {
    private static final Logger logger = LoggerFactory.getLogger(TwitterFollowService.class);

    private final MeEndpoint meEndpoint;
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final FollowEndpoint followEndpoint;
    private final TwitterFollowerCache followerCache;


    public TwitterFollowService(
            MeEndpoint meEndpoint,
            RecentTweetsEndpoint recentTweetsEndpoint,
            FollowEndpoint followEndpoint,
            TwitterFollowerCache followerCache,
            TwitterAccountService twitterAccountService
    ) {
        super(twitterAccountService);
        this.meEndpoint = meEndpoint;
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.followEndpoint = followEndpoint;
        this.followerCache = followerCache;
    }

    @Override
    public void follow() {
        accountService.getAccounts().forEach(account -> {
            logger.info("Looking for tweets for search {} in order to find followers for account {}", account.locationSearch(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.locationSearch()).stream()
                        .filter(tweet -> shouldFollow(tweet, myId, account))
                        .peek(tweet -> logger.debug("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                                tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                        )
                        .forEach(tweet -> followEndpoint.follow(tweet.user(), account));
            } catch (TwitterException e) {
                logger.error("Exception during follow job", e);
            }
        });
    }

    private boolean shouldFollow(Tweet tweet, long myId, TwitterAccount account) {
        return !isTweetFromMe(tweet, myId)
                && isMaybeFromDesiredLocation(tweet, account)
                && !hasBeenSeen(tweet, account)
                && isAuthorBlocked(tweet.user().username(), account);

    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isMaybeFromDesiredLocation(Tweet tweet, TwitterAccount account) {
        return tweet.user().location().toLowerCase().contains(account.locationToFollow().toLowerCase());
    }

    private boolean hasBeenSeen(Tweet tweet, TwitterAccount account) {
        return followerCache.contains(tweet.user().id(), account);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
