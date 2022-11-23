package xyz.nedderhoff.citytweets.api.mastodon;

import org.springframework.web.client.RestTemplate;

@Deprecated(forRemoval = true)
public abstract non-sealed class MastodonApi1Endpoint<T> extends MastodonHttpEndpoint<T> {
    protected static final String BASE_MASTODON_API_1_URI_TEMPLATE = BASE_MASTODON_API_URI_TEMPLATE + "v1/%s";
    public MastodonApi1Endpoint(
            RestTemplate rt
    ) {
        super(rt);
    }
}
