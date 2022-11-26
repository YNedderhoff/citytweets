package xyz.nedderhoff.citytweets.api.twitter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.service.twitter.TwitterAccountService;


// https://developer.twitter.com/en/docs/twitter-api/early-access
public abstract non-sealed class TwitterApi2Endpoint<T> extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_2_URI = BASE_TWITTER_API_URI + "2/";
    private final TwitterAccountService twitterAccountService;

    public TwitterApi2Endpoint(
            RestTemplate rt,
            TwitterAccountService twitterAccountService
    ) {
        super(rt);
        this.twitterAccountService = twitterAccountService;
    }

    protected HttpEntity<T> getResponseHttpEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + twitterAccountService.getRandomBearerTokenTwitter());
        return new HttpEntity<>(headers);
    }
}
