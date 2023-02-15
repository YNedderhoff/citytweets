package xyz.nedderhoff.citytweets.api.twitter.api1;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.concurrent.TimeUnit;

@Component
public class RetweetEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(RetweetEndpoint.class);
    private static final String NAME = "repost_status";

    public RetweetEndpoint(
            RestTemplate rt,
            MetricService metricService,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, metricService, connections);
    }

    public Tweet retweet(Tweet tweet, TwitterAccount account) throws TwitterException {
        final long id = tweet.id();
        logger.debug("Retweeting tweet with id {} ...", id);
        try {
            Stopwatch timer = Stopwatch.createStarted();
            connections.getConnection(account).v1().tweets().retweetStatus(id);
            timer.stop();
            time(timer.elapsed(TimeUnit.MILLISECONDS));
            increment(200);

            logger.info("Successfully retweeted tweet {}", id);
            return tweet;
        } catch (twitter4j.TwitterException e) {
            if (e.getStatusCode() == 403 && e.getErrorCode() == 327) {
                logger.warn("Tweet {} has already been retweeted. Skipping and adding to cache.", id);
                increment(e.getStatusCode());
                return tweet;
            } else {
                logger.error("Error trying to retweet tweet {}", id, e);
                increment(e.getStatusCode());
                throw new TwitterException(e);
            }
        }
    }

    @Override
    public String name() {
        return NAME;
    }
}
