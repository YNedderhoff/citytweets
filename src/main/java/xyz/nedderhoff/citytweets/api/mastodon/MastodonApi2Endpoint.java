package xyz.nedderhoff.citytweets.api.mastodon;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.monitoring.MetricService;


public abstract non-sealed class MastodonApi2Endpoint extends MastodonHttpEndpoint {
    protected static final String BASE_MASTODON_API_2_URI_TEMPLATE = BASE_MASTODON_API_URI_TEMPLATE + "v2/%s";

    public MastodonApi2Endpoint(
            RestTemplate rt,
            MetricService metricService
    ) {
        super(rt, metricService);
    }
}
