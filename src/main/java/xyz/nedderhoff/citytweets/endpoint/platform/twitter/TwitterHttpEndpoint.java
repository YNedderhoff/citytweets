package xyz.nedderhoff.citytweets.endpoint.platform.twitter;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.endpoint.platform.MicrobloggingHttpEndpoint;

public abstract sealed class TwitterHttpEndpoint
        extends MicrobloggingHttpEndpoint
        permits TwitterApi1Endpoint, TwitterApi2Endpoint{
    protected static final String BASE_TWITTER_API_URI = "https://api.twitter.com/";

    public TwitterHttpEndpoint(RestTemplate rt) {
        super(rt);
    }
}
