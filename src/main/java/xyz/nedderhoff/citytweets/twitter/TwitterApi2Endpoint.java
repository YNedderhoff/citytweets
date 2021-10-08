package xyz.nedderhoff.citytweets.twitter;

import org.springframework.web.client.RestTemplate;

// https://developer.twitter.com/en/docs/twitter-api/early-access
public abstract non-sealed class TwitterApi2Endpoint extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_2_URI = BASE_TWITTER_API_URI + "2/";
    protected final String bearerToken;

    public TwitterApi2Endpoint(
            RestTemplate rt,
            String bearerToken
    ) {
        super(rt);
        this.bearerToken = bearerToken;
    }
}
