package xyz.nedderhoff.citytweets.endpoint.platform.twitter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.service.AccountService;


// https://developer.twitter.com/en/docs/twitter-api/early-access
public abstract non-sealed class TwitterApi2Endpoint<T> extends TwitterHttpEndpoint {
    protected static final String BASE_TWITTER_API_2_URI = BASE_TWITTER_API_URI + "2/";
    private final AccountService accountService;

    public TwitterApi2Endpoint(
            RestTemplate rt,
            AccountService accountService
    ) {
        super(rt);
        this.accountService = accountService;
    }

    protected HttpEntity<T> getResponseHttpEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accountService.getRandomBearerToken());
        return new HttpEntity<>(headers);
    }
}
