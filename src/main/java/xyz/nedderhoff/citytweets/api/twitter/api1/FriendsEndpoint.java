package xyz.nedderhoff.citytweets.api.twitter.api1;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.v1.IDs;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class FriendsEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(FriendsEndpoint.class);

    public FriendsEndpoint(
            RestTemplate rt,
            MetricService metricService,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, metricService, connections);
    }

    public Set<Long> getFriends(TwitterAccount account) {
        logger.debug("Fetching friends for account {} ...", account.name());
        long cursor = -1;
        boolean finished = false;
        Set<Long> friendIds = new HashSet<>();

        final Stopwatch timer = Stopwatch.createStarted();
        try {
            while (!finished) {
                logger.debug("Doing iteration with cursor {} for account {}", cursor, account.name());
                IDs friends = connections.getConnection(account).v1().friendsFollowers().getFriendsIDs(cursor);
                long[] ids = friends.getIDs();
                if (ids.length == 0) {
                    logger.info("Full friends list fetched for account {}, total size: {}", account.name(), friendIds.size());
                    finished = true;
                } else {
                    cursor = friends.getNextCursor();
                    Arrays.stream(ids).forEach(friendIds::add);
                }
            }
        } catch (twitter4j.TwitterException e) {
            logger.error("Error trying to fetch twitter followers for account {}", account.name(), e);
            throw new TwitterException(e);
        }
        timer.stop();
        metricService.timeTwitterEndpoint("get_followers", timer.elapsed(TimeUnit.MILLISECONDS));

        return friendIds;
    }
}
