package xyz.nedderhoff.citytweets.api.twitter;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.monitoring.twitter.TwitterMetricService;

@Deprecated(forRemoval = true)
// https://developer.twitter.com/en/docs/twitter-api/v1
public abstract non-sealed class TwitterApi1Endpoint extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_1_URI = BASE_TWITTER_API_URI + "1.1/";
    protected final Twitter4jConnectionsCache connections;

    public TwitterApi1Endpoint(
            RestTemplate rt,
            TwitterMetricService metricService,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, metricService);
        this.connections = connections;
    }
}
