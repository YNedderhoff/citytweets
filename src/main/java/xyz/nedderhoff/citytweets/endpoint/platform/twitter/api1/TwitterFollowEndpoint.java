package xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.TwitterApi1Endpoint;

@Component
public class TwitterFollowEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(TwitterFollowEndpoint.class);
    private final FriendCache friendCache;

    @Autowired
    public TwitterFollowEndpoint(
            FriendCache friendCache,
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
        this.friendCache = friendCache;
    }

    public void follow(User user, TwitterAccount account) {
        logger.info("Following user \"{}\" for account {}", user.name(), account.getName());
        try {
            connections.getConnection(account).createFriendship(user.id());
            logger.info("Successfully followed user {} for account {}", user.name(), account.getName());
            friendCache.add(user.id(), account);
        } catch (TwitterException e) {
            logger.error("Error trying to follow user {} for account {}", user, account.getName(), e);
        }
    }
}
