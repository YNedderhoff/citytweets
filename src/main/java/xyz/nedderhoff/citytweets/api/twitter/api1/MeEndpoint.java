package xyz.nedderhoff.citytweets.api.twitter.api1;

import com.google.common.base.Stopwatch;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.config.Service;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MeEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(MeEndpoint.class);

    public MeEndpoint(
            RestTemplate rt,
            MetricService metricService,
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
            metricService.time(
                    "api_latency",
                    List.of(
                            Tag.of("service", Service.TWITTER.getName()),
                            Tag.of("endpoint", "get_own_id")
                    ),
                    timer.elapsed(TimeUnit.MILLISECONDS)
            );
            return id;

        } catch (twitter4j.TwitterException e) {
            logger.error("Exception while fetching identity for account {}", account.name());
            throw new TwitterException("Exception while fetching identity for account " + account.name(), e);
        }
    }
}
