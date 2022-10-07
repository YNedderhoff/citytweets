package xyz.nedderhoff.citytweets.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.twitter.TwitterApi1Endpoint;

@Component
public class FollowEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(FollowEndpoint.class);
    private final FriendCache friendCache;

    @Autowired
    public FollowEndpoint(
            FriendCache friendCache,
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
        this.friendCache = friendCache;
    }

    public void follow(User user, Account account) {
        logger.info("Following user \"{}\"", user.name());
        try {
            connections.getConnection(account).createFriendship(user.id());
            logger.info("Successfully followed user {}", user.name());
            friendCache.add(user.id(), account);
        } catch (TwitterException e) {
            logger.error("Error trying to follow user {}", user, e);
        }
    }
}
