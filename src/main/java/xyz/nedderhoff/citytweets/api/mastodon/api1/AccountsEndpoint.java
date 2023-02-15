package xyz.nedderhoff.citytweets.api.mastodon.api1;

import com.google.common.base.Stopwatch;
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
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AccountsEndpoint extends MastodonApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(AccountsEndpoint.class);

    public AccountsEndpoint(RestTemplate rt, MetricService metricService) {
        super(rt, metricService);
    }

    public Set<String> getFollowers(MastodonAccount mastodonAccount) {
        logger.debug("Fetching followers for account {}", mastodonAccount.name());
        final HttpHeaders authedHeaders = getHttpHeadersWithAuth(mastodonAccount);
        final HttpEntity<Account[]> request = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("accounts/%s/followers", mastodonAccount.id())
        );

        String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .encode()
                .toUriString();

        Stopwatch timer = Stopwatch.createStarted();
        final ResponseEntity<Account[]> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.GET,
                request,
                Account[].class
        );
        timer.stop();
        metricService.timeMastodonEndpoint("get_followers", timer.elapsed(TimeUnit.MILLISECONDS));

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Collections.emptySet();
        }

        return Arrays.stream(response.getBody()).map(Account::id).collect(Collectors.toSet());
    }

    public List<Status> getStatuses(String followerId, MastodonAccount mastodonAccount) {
        logger.debug("Fetching statuses for follower {} of account {}", followerId, mastodonAccount.name());
        final HttpHeaders authedHeaders = getHttpHeadersWithAuth(mastodonAccount);
        final HttpEntity<Account[]> request = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("accounts/%s/statuses", followerId)
        );

        final String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .encode()
                .toUriString();

        Stopwatch timer = Stopwatch.createStarted();
        final ResponseEntity<Status[]> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.GET,
                request,
                Status[].class
        );
        timer.stop();
        metricService.timeMastodonEndpoint("get_statuses", timer.elapsed(TimeUnit.MILLISECONDS));


        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }
}
