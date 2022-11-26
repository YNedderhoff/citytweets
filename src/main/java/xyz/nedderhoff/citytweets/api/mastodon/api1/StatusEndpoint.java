package xyz.nedderhoff.citytweets.api.mastodon.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi1Endpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Account;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;
import xyz.nedderhoff.citytweets.exception.mastodon.MastodonException;

@Component
public class StatusEndpoint extends MastodonApi1Endpoint<Account[]> {
    private static final Logger logger = LoggerFactory.getLogger(StatusEndpoint.class);

    public StatusEndpoint(RestTemplate rt) {
        super(rt);
    }
    //POST https://mastodon.example/api/v1/statuses/:id/reblog HTTP/1.1
    public Status boost(Status status, HttpHeaders authedHeaders, MastodonAccount mastodonAccount) throws MastodonException {
        logger.info("Boosting status {} for {}", status.uri(), mastodonAccount.name());

        final HttpEntity<Account[]> request = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("statuses/%s/reblog", status.id())
        );

        String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .encode()
                .toUriString();

        final ResponseEntity<Status> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.POST,
                request,
                Status.class
        );

        if (response.getBody() == null) {
            final HttpStatus statusCode = response.getStatusCode();
            throw new MastodonException(String.format("Error boosting status. Status code %s: %s", statusCode, response));
        }
        return response.getBody();
    }
}
