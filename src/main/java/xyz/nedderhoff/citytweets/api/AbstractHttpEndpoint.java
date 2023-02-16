package xyz.nedderhoff.citytweets.api;

import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

public abstract class AbstractHttpEndpoint<MetricServiceType extends MetricService>
        implements HttpEndpoint<MetricServiceType> {
    protected final RestTemplate rt;
    protected final MetricServiceType metricService;

    public AbstractHttpEndpoint(RestTemplate rt, MetricServiceType metricService) {
        this.rt = rt;
        this.metricService = metricService;
    }
}
