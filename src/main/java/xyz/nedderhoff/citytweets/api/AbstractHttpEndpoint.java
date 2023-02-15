package xyz.nedderhoff.citytweets.api;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

public abstract class AbstractHttpEndpoint implements HttpEndpoint{
    protected final RestTemplate rt;
    protected final MetricService metricService;

    public AbstractHttpEndpoint(RestTemplate rt, MetricService metricService) {
        this.rt = rt;
        this.metricService = metricService;
    }


}
