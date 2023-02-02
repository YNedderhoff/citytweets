package xyz.nedderhoff.citytweets.api.mastodon.api2;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi2Endpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Auth;

import java.util.Optional;

@Component
public class AuthEndpoint extends MastodonApi2Endpoint<Auth> {
    private static final Logger logger = LoggerFactory.getLogger(AuthEndpoint.class);

    public AuthEndpoint(RestTemplate rt) {
        super(rt);
    }

    public Optional<HttpHeaders> getHttpHeadersWithAuth(MastodonAccount mastodonAccount) {
        JSONObject body = new JSONObject();
        body.put("client_id", mastodonAccount.oauth().accessToken());
        body.put("client_secret", mastodonAccount.oauth().accessTokenSecret());
        body.put("redirect_uri", mastodonAccount.redirectUri());
        body.put("code", mastodonAccount.oauth().authorizationCode());
        body.put("scope", "read write follow");
        body.put("grant_type", "authorization_code");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        String requestUri = String.format(
                "https://%s/%s",
                mastodonAccount.instance(),
                "oauth/token"
        );

        final ResponseEntity<Auth> response = rt.exchange(
                requestUri,
                HttpMethod.POST,
                request,
                Auth.class
        );

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Optional.empty();
        }

        final HttpHeaders authedHeaders = new HttpHeaders();
        authedHeaders.setContentType(MediaType.APPLICATION_JSON);
        authedHeaders.set("Authorization", "Bearer " + response.getBody().accessToken());
        return Optional.of(authedHeaders);
    }
}
