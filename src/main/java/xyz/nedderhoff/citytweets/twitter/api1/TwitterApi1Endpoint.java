package xyz.nedderhoff.citytweets.twitter.api1;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.twitter.TwitterHttpEndpoint;

@Deprecated
// https://developer.twitter.com/en/docs/twitter-api/v1
public abstract class TwitterApi1Endpoint extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_1_URI = BASE_TWITTER_API_URI + "1.1/";

    public TwitterApi1Endpoint(
            RestTemplate rt
    ) {
        super(rt);
    }
}
