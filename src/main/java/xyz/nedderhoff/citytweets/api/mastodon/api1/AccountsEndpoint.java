package xyz.nedderhoff.citytweets.api.mastodon.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi1Endpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Account;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class AccountsEndpoint extends MastodonApi1Endpoint<Account[]> {
    private static final Logger logger = LoggerFactory.getLogger(AccountsEndpoint.class);

    public AccountsEndpoint(RestTemplate rt) {
        super(rt);
    }

    public List<Account> getFollowers(String mastodonAccountId, HttpHeaders authedHeaders, MastodonAccount mastodonAccount) {
        logger.info("Fetching followers for account {}", mastodonAccount.name());
        final HttpEntity<Account[]> request = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("accounts/%s/followers", mastodonAccountId)
        );

        String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .encode()
                .toUriString();

        final ResponseEntity<Account[]> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.GET,
                request,
                Account[].class
        );

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }

    public List<Status> getStatuses(Account account, HttpHeaders oauthTokenHeaders, MastodonAccount mastodonAccount) {
        logger.info("Fetching statuses for follower {} of account {}", account.id(), mastodonAccount.name());
        final HttpEntity<Account[]> request = new HttpEntity<>(oauthTokenHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("accounts/%s/statuses", account.id())
        );

        final String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .encode()
                .toUriString();

        final ResponseEntity<Status[]> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.GET,
                request,
                Status[].class
        );

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}
