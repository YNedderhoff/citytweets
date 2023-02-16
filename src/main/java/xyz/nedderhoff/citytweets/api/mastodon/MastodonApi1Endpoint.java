package xyz.nedderhoff.citytweets.api.mastodon;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.monitoring.mastodon.MastodonMetricService;

@Deprecated(forRemoval = true)
public abstract non-sealed class MastodonApi1Endpoint extends MastodonHttpEndpoint {
    protected static final String BASE_MASTODON_API_1_URI_TEMPLATE = BASE_MASTODON_API_URI_TEMPLATE + "v1/%s";

    public MastodonApi1Endpoint(
            RestTemplate rt,
            MastodonMetricService metricService
    ) {
        super(rt, metricService);
    }
}
