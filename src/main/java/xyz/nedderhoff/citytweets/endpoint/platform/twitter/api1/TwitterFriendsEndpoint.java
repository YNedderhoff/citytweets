package xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.IDs;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.TwitterApi1Endpoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class TwitterFriendsEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(TwitterFriendsEndpoint.class);

    @Autowired
    public TwitterFriendsEndpoint(
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
    }

    public Set<Long> getFriends(TwitterAccount account) throws TwitterException {
        logger.info("Fetching friends for account {} ...", account.getName());
        long cursor = -1;
        boolean finished = false;
        Set<Long> friendIds = new HashSet<>();
        while (!finished) {
            logger.info("Doing iteration with cursor {} for account {}", cursor, account.getName());
            IDs friends = connections.getConnection(account).getFriendsIDs(cursor);
            long[] ids = friends.getIDs();
            if (ids.length == 0) {
                logger.info("Full friends list fetched for account {}, total size: {}", account.getName(), friendIds.size());
                finished = true;
            } else {
                cursor = friends.getNextCursor();
                Arrays.stream(ids).forEach(friendIds::add);
            }
        }
        return friendIds;
    }
}
