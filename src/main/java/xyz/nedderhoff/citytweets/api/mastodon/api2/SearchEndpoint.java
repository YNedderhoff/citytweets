package xyz.nedderhoff.citytweets.api.mastodon.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi2Endpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Account;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Search;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SearchEndpoint extends MastodonApi2Endpoint<Search> {
    private static final Logger logger = LoggerFactory.getLogger(SearchEndpoint.class);

    public SearchEndpoint(RestTemplate rt) {
        super(rt);
    }

    public Optional<String> searchAccountId(HttpHeaders authedHeaders, MastodonAccount mastodonAccount) {
        final HttpEntity<Search> responseEntity = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_2_URI_TEMPLATE,
                mastodonAccount.instance(),
                "search"
        );

        String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .queryParam("q", "{q}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("q", mastodonAccount.name());

        final ResponseEntity<Search> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.GET,
                responseEntity,
                Search.class,
                params
        );

        if (response.getBody() == null) {
            logger.warn("Got response with status {}: {}", response.getStatusCode(), response);
            return Optional.empty();
        }

        return response.getBody()
                .accounts()
                .stream()
                .filter(account -> account.name().equals(mastodonAccount.name()))
                .map(Account::id).findFirst();
    }


}
