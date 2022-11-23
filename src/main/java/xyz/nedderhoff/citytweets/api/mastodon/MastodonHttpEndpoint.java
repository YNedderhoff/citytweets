package xyz.nedderhoff.citytweets.api.mastodon;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.HttpEndpoint;

// https://docs.joinmastodon.org/
public abstract sealed class MastodonHttpEndpoint<T> extends HttpEndpoint permits MastodonApi1Endpoint, MastodonApi2Endpoint {
    protected static final String BASE_MASTODON_API_URI_TEMPLATE = "https://%s/api/";

    public MastodonHttpEndpoint(RestTemplate rt) {
        super(rt);
    }


}
