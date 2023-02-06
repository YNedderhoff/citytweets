package xyz.nedderhoff.citytweets.api.mastodon.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;

public class MastodonAuthUtils {

    public static HttpHeaders getHttpHeadersWithAuth(MastodonAccount mastodonAccount) {
        final HttpHeaders authedHeaders = new HttpHeaders();
        authedHeaders.setContentType(MediaType.APPLICATION_JSON);
        authedHeaders.set("Authorization", "Bearer " + mastodonAccount.oauth().bearerToken());
        return authedHeaders;
    }
}
