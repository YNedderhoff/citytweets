package xyz.nedderhoff.citytweets.api.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.IDs;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class FriendsEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(FriendsEndpoint.class);

    public FriendsEndpoint(
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
    }

    public Set<Long> getFriends(TwitterAccount account) throws TwitterException {
        logger.info("Fetching friends for account {} ...", account.name());
        long cursor = -1;
        boolean finished = false;
        Set<Long> friendIds = new HashSet<>();
        while (!finished) {
            logger.info("Doing iteration with cursor {} for account {}", cursor, account.name());
            IDs friends = connections.getConnection(account).getFriendsIDs(cursor);
            long[] ids = friends.getIDs();
            if (ids.length == 0) {
                logger.info("Full friends list fetched for account {}, total size: {}", account.name(), friendIds.size());
                finished = true;
            } else {
                cursor = friends.getNextCursor();
                Arrays.stream(ids).forEach(friendIds::add);
            }
        }
        return friendIds;
    }
}
