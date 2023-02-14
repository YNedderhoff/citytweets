package xyz.nedderhoff.citytweets.api.mastodon.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi2Endpoint;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

@Component
public class SearchEndpoint extends MastodonApi2Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(SearchEndpoint.class);

    public SearchEndpoint(RestTemplate rt, MetricService metricService) {
        super(rt, metricService);
    }

}
