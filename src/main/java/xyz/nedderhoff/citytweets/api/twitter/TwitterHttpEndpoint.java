package xyz.nedderhoff.citytweets.api.twitter;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.AbstractHttpEndpoint;
import xyz.nedderhoff.citytweets.monitoring.twitter.TwitterMetricService;

public abstract sealed class TwitterHttpEndpoint extends AbstractHttpEndpoint<TwitterMetricService> permits TwitterApi1Endpoint, TwitterApi2Endpoint {
    protected static final String BASE_TWITTER_API_URI = "https://api.twitter.com/";

    public TwitterHttpEndpoint(RestTemplate rt, TwitterMetricService metricService) {
        super(rt, metricService);
    }

    @Override
    public void time(long t) {
        metricService.timeEndpoint(name(), t);
    }

    @Override
    public void increment(int statusCode) {
        metricService.incrementEndpoint(name(), statusCode);
    }
}
