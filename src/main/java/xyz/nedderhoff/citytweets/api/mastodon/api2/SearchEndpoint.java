package xyz.nedderhoff.citytweets.api.mastodon.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi2Endpoint;
import xyz.nedderhoff.citytweets.monitoring.mastodon.MastodonMetricService;

@Component
public class SearchEndpoint extends MastodonApi2Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(SearchEndpoint.class);
    private static final String NAME = "search";

    public SearchEndpoint(RestTemplate rt, MastodonMetricService metricService) {
        super(rt, metricService);
    }

    @Override
    public String name() {
        return NAME;
    }
}
