package xyz.nedderhoff.citytweets.endpoint.platform.twitter;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;

@Deprecated(forRemoval = true)
// https://developer.twitter.com/en/docs/twitter-api/v1
public abstract non-sealed class TwitterApi1Endpoint extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_1_URI = BASE_TWITTER_API_URI + "1.1/";
    protected final Twitter4jConnectionsCache connections;

    public TwitterApi1Endpoint(
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt);
        this.connections = connections;
    }
}
