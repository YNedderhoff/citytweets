package xyz.nedderhoff.citytweets.api.twitter;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.AbstractHttpEndpoint;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

public abstract sealed class TwitterHttpEndpoint extends AbstractHttpEndpoint permits TwitterApi1Endpoint, TwitterApi2Endpoint {
    protected static final String BASE_TWITTER_API_URI = "https://api.twitter.com/";

    public TwitterHttpEndpoint(RestTemplate rt, MetricService metricService) {
        super(rt, metricService);
    }

    @Override
    public void time(long t) {
        metricService.timeTwitterEndpoint(name(), t);
    }

    @Override
    public void increment(int statusCode) {
        metricService.incrementTwitterEndpoint(name(), statusCode);
    }
}
