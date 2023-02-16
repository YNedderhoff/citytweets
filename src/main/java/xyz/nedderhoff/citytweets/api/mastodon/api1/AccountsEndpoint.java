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
import xyz.nedderhoff.citytweets.cache.mastodon.RetootCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Account;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;
import xyz.nedderhoff.citytweets.monitoring.mastodon.MastodonMetricService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// TODO split out two endpoints into separate classes and properly use time and increment methods like elsewhere
@Component
public class AccountsEndpoint extends MastodonApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(AccountsEndpoint.class);
    private static final String NAME = "get_followers";

    private final RetootCache retootCache;

    public AccountsEndpoint(RestTemplate rt, MastodonMetricService metricService, RetootCache retootCache) {
        super(rt, metricService);
        this.retootCache = retootCache;
    }

    public Set<Long> getFollowers(MastodonAccount mastodonAccount) {
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
        time(timer.elapsed(TimeUnit.MILLISECONDS));
        increment(response.getStatusCode().value());

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Collections.emptySet();
        }

        return Arrays.stream(response.getBody()).map(Account::id).collect(Collectors.toSet());
    }

    public List<Status> getStatuses(Long followerId, MastodonAccount mastodonAccount) {
        final Long highestId = retootCache.getHighestId(mastodonAccount);
        logger.debug("Fetching statuses for follower {} of account {} with ids higher than {}",
                followerId, mastodonAccount.name(), highestId);
        final HttpHeaders authedHeaders = getHttpHeadersWithAuth(mastodonAccount);
        final HttpEntity<Account[]> request = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("accounts/%s/statuses", followerId)
        );

        final String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .queryParam("since_id", "{sinceId}")
                .encode()
                .toUriString();

        Map<String, Long> params = new HashMap<>();
        params.put("sinceId", highestId);

        Stopwatch timer = Stopwatch.createStarted();
        final ResponseEntity<Status[]> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.GET,
                request,
                Status[].class,
                params
        );
        timer.stop();
        metricService.timeEndpoint("get_statuses", timer.elapsed(TimeUnit.MILLISECONDS));
        metricService.incrementEndpoint("get_statuses", response.getStatusCode().value());

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }

    @Override
    public String name() {
        return NAME;
    }
}
