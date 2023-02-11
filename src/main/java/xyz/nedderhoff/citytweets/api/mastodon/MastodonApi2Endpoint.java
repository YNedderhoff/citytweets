package xyz.nedderhoff.citytweets.api.mastodon;

import org.springframework.web.client.RestTemplate;


public abstract non-sealed class MastodonApi2Endpoint extends MastodonHttpEndpoint {
    protected static final String BASE_MASTODON_API_2_URI_TEMPLATE = BASE_MASTODON_API_URI_TEMPLATE + "v2/%s";

    public MastodonApi2Endpoint(
            RestTemplate rt
    ) {
        super(rt);
    }
}
