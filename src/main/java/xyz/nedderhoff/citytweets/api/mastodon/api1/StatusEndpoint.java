package xyz.nedderhoff.citytweets.api.mastodon.api1;

import com.google.common.base.Stopwatch;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi1Endpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.config.Service;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Account;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;
import xyz.nedderhoff.citytweets.exception.mastodon.MastodonException;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class StatusEndpoint extends MastodonApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(StatusEndpoint.class);

    public StatusEndpoint(RestTemplate rt, MetricService metricService) {
        super(rt, metricService);
    }

    public Status boost(Status status, MastodonAccount mastodonAccount) throws MastodonException {
        logger.info("Boosting status {} for {}", status.uri(), mastodonAccount.name());
        final HttpHeaders authedHeaders = getHttpHeadersWithAuth(mastodonAccount);
        final HttpEntity<Account[]> request = new HttpEntity<>(authedHeaders);

        String requestUri = String.format(
                BASE_MASTODON_API_1_URI_TEMPLATE,
                mastodonAccount.instance(),
                String.format("statuses/%s/reblog", status.id())
        );

        String requestUriTemplate = UriComponentsBuilder.fromHttpUrl(requestUri)
                .encode()
                .toUriString();

        Stopwatch timer = Stopwatch.createStarted();
        final ResponseEntity<Status> response = rt.exchange(
                requestUriTemplate,
                HttpMethod.POST,
                request,
                Status.class
        );
        timer.stop();
        metricService.time(
                "api_latency",
                List.of(
                        Tag.of("service", Service.MASTODON.getName()),
                        Tag.of("endpoint", "repost_status")
                ),
                timer.elapsed(TimeUnit.MILLISECONDS)
        );


        if (response.getBody() == null) {
            final HttpStatusCode statusCode = response.getStatusCode();
            throw new MastodonException(String.format("Error boosting status. Status code %s: %s", statusCode, response));
        }
        return response.getBody();
    }
}
