package xyz.nedderhoff.citytweets.api;

import xyz.nedderhoff.citytweets.monitoring.MetricService;

public interface HttpEndpoint<MetricServiceType extends MetricService> {

    void time(long t);
    void increment(int statusCode);

    String name();
}
