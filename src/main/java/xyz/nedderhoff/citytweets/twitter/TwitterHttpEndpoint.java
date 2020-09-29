package xyz.nedderhoff.citytweets.twitter;

import org.springframework.web.client.RestTemplate;

public abstract class TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_URI = "https://api.twitter.com/";
    protected final RestTemplate rt;

    public TwitterHttpEndpoint(RestTemplate rt) {
        this.rt = rt;
    }
}
