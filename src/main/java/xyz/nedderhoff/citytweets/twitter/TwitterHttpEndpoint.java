package xyz.nedderhoff.citytweets.twitter;

import org.springframework.web.client.RestTemplate;

public abstract sealed class TwitterHttpEndpoint permits TwitterApi1Endpoint, TwitterApi2Endpoint{
    protected static final String BASE_TWITTER_API_URI = "https://api.twitter.com/";
    protected final RestTemplate rt;

    public TwitterHttpEndpoint(RestTemplate rt) {
        this.rt = rt;
    }
}
