package xyz.nedderhoff.citytweets.api.twitter.api1;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.monitoring.twitter.TwitterMetricService;

import java.util.concurrent.TimeUnit;

@Component
public class MeEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(MeEndpoint.class);
    private static final String NAME = "get_own_id";

    public MeEndpoint(
            RestTemplate rt,
            TwitterMetricService metricService,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, metricService, connections);
    }

    public long getId(TwitterAccount account) throws TwitterException {
        logger.debug("Fetching own id for account {} ...", account.name());
        try {
            Stopwatch timer = Stopwatch.createStarted();
            final long id = connections.getConnection(account).v1().users().verifyCredentials().getId();
            timer.stop();
            time(timer.elapsed(TimeUnit.MILLISECONDS));
            increment(200);
            return id;
        } catch (twitter4j.TwitterException e) {
            logger.error("Exception while fetching identity for account {}", account.name());
            increment(e.getStatusCode());
            throw new TwitterException("Exception while fetching identity for account " + account.name(), e);
        }
    }

    @Override
    public String name() {
        return NAME;
    }
}
