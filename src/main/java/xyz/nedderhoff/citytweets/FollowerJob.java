package xyz.nedderhoff.citytweets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@Component
@EnableScheduling
public class FollowerJob {
    private static final Logger logger = LoggerFactory.getLogger(FollowerJob.class);
    private static final int FOLLOW_RATE = 1000 * 60 * 60 * 24;

    private final Twitter twitter;
    private final String locationSearch;
    private final String locationToFollow;
    private final String ownHandle;

    @Autowired
    public FollowerJob(Twitter twitter,
                       @Value("${location-search}") String locationSearch,
                       @Value("${location-to-follow}") String locationToFollow,
                       @Value("${own-handle}") String ownHandle) {
        this.twitter = twitter;
        this.locationSearch = locationSearch;
        this.locationToFollow = locationToFollow;
        this.ownHandle = ownHandle;
    }

    @Scheduled(fixedRate = FOLLOW_RATE)
    public void findPotentialFollowers() throws TwitterException {
        logger.info("Looking for tweets for search {} in order to find followers", locationSearch);

        Query query = new Query(locationSearch);
        query.setCount(100);
        QueryResult result = twitter.search(query);

        result.getTweets().stream()
                .filter(tweet -> !tweet.getUser().getName().toLowerCase().equals(ownHandle.toLowerCase()))
                .filter(tweet -> tweet.getUser().getLocation().toLowerCase().contains(locationToFollow.toLowerCase()))
                .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                        tweet.getId(), tweet.getUser().getName(), tweet.getLang(), tweet.getUser().getLocation(), tweet.getText())
                )
                .forEach(tweet -> retweet(tweet.getUser()));
    }

    private void retweet(User user) {
        logger.info("Following user \"{}\"", user.getName());
        try {
            twitter.createFriendship(user.getId());
            logger.info("Successfully followed user {}", user.getName());
        } catch (TwitterException e) {
            logger.error("Error trying to follow user {}", user, e);
        }
    }
}
