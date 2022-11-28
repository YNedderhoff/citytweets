package xyz.nedderhoff.citytweets.service.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.api.twitter.api1.FollowEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.cache.twitter.FriendCache;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.service.FollowService;

public class TwitterFollowService implements FollowService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterFollowService.class);

    private final MeEndpoint meEndpoint;
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final FollowEndpoint followEndpoint;
    private final FriendCache friendCache;
    private final TwitterAccountService twitterAccountService;

    public TwitterFollowService(
            MeEndpoint meEndpoint,
            RecentTweetsEndpoint recentTweetsEndpoint,
            FollowEndpoint followEndpoint,
            FriendCache friendCache,
            TwitterAccountService twitterAccountService
    ) {
        this.meEndpoint = meEndpoint;
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.followEndpoint = followEndpoint;
        this.friendCache = friendCache;
        this.twitterAccountService = twitterAccountService;
    }

    @Override
    public void follow() {
        twitterAccountService.getAccounts().forEach(account -> {
            logger.info("Looking for tweets for search {} in order to find followers for account {}", account.locationSearch(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.locationSearch()).stream()
                        .filter(tweet -> shouldFollow(tweet, myId, account))
                        .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                                tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                        )
                        .forEach(tweet -> followEndpoint.follow(tweet.user(), account));
            } catch (TwitterException e) {
                logger.error("Exception during follow job", e);
            }
        });
    }

    private boolean shouldFollow(Tweet tweet, long myId, AccountProperties.TwitterAccount account) {
        return !isTweetFromMe(tweet, myId)
                && isMaybeFromDesiredLocation(tweet, account)
                && !hasBeenSeen(tweet, account);

    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isMaybeFromDesiredLocation(Tweet tweet, AccountProperties.TwitterAccount account) {
        return tweet.user().location().toLowerCase().contains(account.locationToFollow().toLowerCase());
    }

    private boolean hasBeenSeen(Tweet tweet, AccountProperties.TwitterAccount account) {
        return friendCache.contains(tweet.user().id(), account);
    }
}
