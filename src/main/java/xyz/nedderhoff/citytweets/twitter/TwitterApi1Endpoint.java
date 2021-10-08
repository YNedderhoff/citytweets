package xyz.nedderhoff.citytweets.twitter;

import org.springframework.web.client.RestTemplate;

@Deprecated(forRemoval = true)
// https://developer.twitter.com/en/docs/twitter-api/v1
public abstract non-sealed class TwitterApi1Endpoint extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_1_URI = BASE_TWITTER_API_URI + "1.1/";

    public TwitterApi1Endpoint(
            RestTemplate rt
    ) {
        super(rt);
    }
}
