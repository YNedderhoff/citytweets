package xyz.nedderhoff.citytweets.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.cache.RetweetCache;

@Component
public class TwitterService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    private final Twitter twitter;
    private final RetweetCache retweetCache;
    private final FriendCache friendCache;

    @Autowired
    public TwitterService(Twitter twitter, RetweetCache retweetCache, FriendCache friendCache) {
        this.twitter = twitter;
        this.retweetCache = retweetCache;
        this.friendCache = friendCache;
    }

    public long getId() throws TwitterException {
        return twitter.getId();
    }

    public QueryResult search(Query query) throws TwitterException {
        return twitter.search(query);
    }

    public void retweet(Status tweet) {
        long id = tweet.getId();
        logger.info("Retweeting tweet with id {} ...", id);
        try {
            twitter.retweetStatus(id);
            logger.info("Successfully retweeted tweet {}", id);
            retweetCache.add(id);
        } catch (TwitterException e) {
            if (e.getStatusCode() == 403 && e.getErrorCode() == 327) {
                logger.warn("Tweet {} has already been retweeted. Skipping and adding to cache.", id);
                retweetCache.add(id);
            } else {
                logger.error("Error trying to retweet tweet {}", id, e);
            }
        }
    }

    public void follow(Status tweet) {
        User user = tweet.getUser();
        logger.info("Following user \"{}\"", user.getName());
        try {
            twitter.createFriendship(user.getId());
            logger.info("Successfully followed user {}", user.getName());
            friendCache.add(user.getId());
        } catch (TwitterException e) {
            logger.error("Error trying to follow user {}", user, e);
        }
    }
}
