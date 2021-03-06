package xyz.nedderhoff.citytweets.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.domain.User;

@Component
public class FollowEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(FollowEndpoint.class);
    private final Twitter twitter;
    private final FriendCache friendCache;

    @Autowired
    public FollowEndpoint(
            FriendCache friendCache,
            RestTemplate rt,
            Twitter twitter
    ) {
        super(rt);
        this.friendCache = friendCache;
        this.twitter = twitter;
    }

    public void follow(User user) {
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
