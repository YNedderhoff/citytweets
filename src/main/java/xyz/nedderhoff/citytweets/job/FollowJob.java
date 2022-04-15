package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.twitter.api1.FollowEndpoint;
import xyz.nedderhoff.citytweets.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.twitter.api2.RecentTweetsEndpoint;

@Component
@EnableScheduling
public class FollowJob {
    private static final Logger logger = LoggerFactory.getLogger(FollowJob.class);
    private static final int FOLLOW_RATE = 1000 * 60 * 60 * 24;

    private final MeEndpoint meEndpoint;
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final FollowEndpoint followEndpoint;
    private final FriendCache friendCache;
    private final AccountProperties accountProperties;
    private final String locationToFollow;
    private final String query;

    @Autowired
    public FollowJob(
            MeEndpoint meEndpoint,
            RecentTweetsEndpoint recentTweetsEndpoint,
            FollowEndpoint followEndpoint,
            FriendCache friendCache,
            AccountProperties accountProperties,
            @Value("${location-search}") String locationSearch,
            @Value("${location-to-follow}") String locationToFollow
    ) {
        this.meEndpoint = meEndpoint;
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.followEndpoint = followEndpoint;
        this.friendCache = friendCache;
        this.accountProperties = accountProperties;
        this.locationToFollow = locationToFollow;
        this.query = locationSearch;
    }

    @Scheduled(fixedRate = FOLLOW_RATE)
    public void findPotentialFollowers() throws TwitterException {
        logger.info("Looking for tweets for search {} in order to find followers", query);
        final long myId = meEndpoint.getId();

        recentTweetsEndpoint.search(query).stream()
                .filter(tweet -> shouldFollow(tweet, myId))
                .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                        tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                )
                .forEach(tweet -> followEndpoint.follow(tweet.user()));
    }

    private boolean shouldFollow(Tweet tweet, long myId) {
        return !isTweetFromMe(tweet, myId)
                && isMaybeFromDesiredLocation(tweet)
                && !hasBeenSeen(tweet);

    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isMaybeFromDesiredLocation(Tweet tweet) {
        return tweet.user().location().toLowerCase().contains(locationToFollow.toLowerCase());
    }

    private boolean hasBeenSeen(Tweet tweet) {
        return friendCache.contains(tweet.user().id());
    }
}
