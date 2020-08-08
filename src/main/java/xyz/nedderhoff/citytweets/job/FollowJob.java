package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.twitter.TwitterService;

@Component
@EnableScheduling
public class FollowJob {
    private static final Logger logger = LoggerFactory.getLogger(FollowJob.class);
    private static final int FOLLOW_RATE = 1000 * 60 * 60 * 24;

    private final TwitterService twitter;
    private final FriendCache friendCache;
    private final String locationToFollow;
    private final Query query;

    @Autowired
    public FollowJob(TwitterService twitter,
                     FriendCache friendCache,
                     @Value("${location-search}") String locationSearch,
                     @Value("${location-to-follow}") String locationToFollow) {
        this.friendCache = friendCache;
        this.twitter = twitter;
        this.locationToFollow = locationToFollow;

        this.query = new Query(locationSearch);
        this.query.setCount(100);
    }

    @Scheduled(fixedRate = FOLLOW_RATE)
    public void findPotentialFollowers() throws TwitterException {
        logger.info("Looking for tweets for search {} in order to find followers", query.getQuery());
        long myId = twitter.getId();

        twitter.search(query).getTweets().stream()
                .filter(tweet -> shouldFollow(tweet, myId))
                .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                        tweet.getId(), tweet.getUser().getName(), tweet.getLang(), tweet.getUser().getLocation(), tweet.getText())
                )
                .forEach(twitter::follow);
    }

    private boolean shouldFollow(Status tweet, long myId) {
        return !isTweetFromMe(tweet, myId)
                && isMaybeFromDesiredLocation(tweet)
                && !hasBeenSeen(tweet);

    }

    private boolean isTweetFromMe(Status tweet, long myId) {
        return tweet.getUser().getId() == myId;
    }

    private boolean isMaybeFromDesiredLocation(Status tweet) {
        return tweet.getUser().getLocation().toLowerCase().contains(locationToFollow.toLowerCase());
    }

    private boolean hasBeenSeen(Status tweet) {
        return friendCache.contains(tweet.getUser().getId());
    }
}
