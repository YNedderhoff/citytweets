package xyz.nedderhoff.citytweets.api.mastodon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;

public class MastodonAuthUtils {
    private static final Logger logger = LoggerFactory.getLogger(MastodonAuthUtils.class);

    public static HttpHeaders getHttpHeadersWithAuth(MastodonAccount mastodonAccount) {
        final HttpHeaders authedHeaders = new HttpHeaders();
        authedHeaders.setContentType(MediaType.APPLICATION_JSON);
        authedHeaders.set("Authorization", "Bearer " + mastodonAccount.oauth().bearerToken());
        return authedHeaders;
    }
}
