package xyz.nedderhoff.citytweets.api.twitter.api1;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.cache.twitter.TwitterFollowerCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.domain.twitter.User;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.concurrent.TimeUnit;

@Component
public class FollowEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(FollowEndpoint.class);
    private static final String NAME = "follow";
    private final TwitterFollowerCache followerCache;

    public FollowEndpoint(
            TwitterFollowerCache followerCache,
            RestTemplate rt,
            MetricService metricService,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, metricService, connections);
        this.followerCache = followerCache;
    }

    public void follow(User user, TwitterAccount account) {
        logger.debug("Following user \"{}\" for account {}", user.name(), account.name());
        try {
            Stopwatch timer = Stopwatch.createStarted();
            connections.getConnection(account).v1().friendsFollowers().createFriendship(user.id());
            timer.stop();
            time(timer.elapsed(TimeUnit.MILLISECONDS));
            increment(200);

            logger.info("Successfully followed user {} for account {}", user.name(), account.name());
            followerCache.add(user.id(), account);
        } catch (TwitterException e) {
            logger.error("Error trying to follow user {} for account {}", user, account.name(), e);
            increment(e.getStatusCode());
        }
    }

    @Override
    public String name() {
        return NAME;
    }
}
