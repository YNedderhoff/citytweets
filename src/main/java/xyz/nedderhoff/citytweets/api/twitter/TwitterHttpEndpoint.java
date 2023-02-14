package xyz.nedderhoff.citytweets.api.twitter;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.HttpEndpoint;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

public abstract sealed class TwitterHttpEndpoint extends HttpEndpoint permits TwitterApi1Endpoint, TwitterApi2Endpoint {
    protected static final String BASE_TWITTER_API_URI = "https://api.twitter.com/";

    public TwitterHttpEndpoint(RestTemplate rt, MetricService metricService) {
        super(rt, metricService);
    }
}
