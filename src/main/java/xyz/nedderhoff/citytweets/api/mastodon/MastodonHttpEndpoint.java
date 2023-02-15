package xyz.nedderhoff.citytweets.api.mastodon;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.AbstractHttpEndpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

// https://docs.joinmastodon.org/
public abstract sealed class MastodonHttpEndpoint extends AbstractHttpEndpoint permits MastodonApi1Endpoint, MastodonApi2Endpoint {
    protected static final String BASE_MASTODON_API_URI_TEMPLATE = "https://%s/api/";

    public MastodonHttpEndpoint(RestTemplate rt, MetricService metricService) {
        super(rt, metricService);
    }

    protected HttpHeaders getHttpHeadersWithAuth(MastodonAccount mastodonAccount) {
        final HttpHeaders authedHeaders = new HttpHeaders();
        authedHeaders.setContentType(MediaType.APPLICATION_JSON);
        authedHeaders.set("Authorization", "Bearer " + mastodonAccount.oauth().bearerToken());
        return authedHeaders;
    }

    @Override
    public void time(long t) {
        metricService.timeMastodonEndpoint(name(), t);
    }

    @Override
    public void increment(int statusCode) {
        metricService.incrementMastodonEndpoint(name(), statusCode);
    }
}
