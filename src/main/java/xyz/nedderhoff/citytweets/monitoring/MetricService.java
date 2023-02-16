package xyz.nedderhoff.citytweets.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import xyz.nedderhoff.citytweets.config.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class MetricService {

    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;
    private final MeterRegistry meterRegistry;
    private final Service service;

    public MetricService(MeterRegistry meterRegistry, Service service) {
        this.meterRegistry = meterRegistry;
        this.service = service;
    }

    public void timeEndpoint(String name, long t) {
        time(
                "api_latency",
                List.of(
                        Tag.of("service", service.getName()),
                        Tag.of("endpoint", name)
                ),
                t
        );
    }

    public void time(String name, long t) {
        time(name, Collections.emptySet(), t);
    }

    public void time(String name, Iterable<Tag> tags, long t) {
        final Timer timer = timer(name, tags).register(meterRegistry);
        timer.record(t, DEFAULT_TIME_UNIT);
    }

    public void incrementEndpoint(String name, int statusCode) {
        increment(
                "api_call",
                List.of(
                        Tag.of("service", service.getName()),
                        Tag.of("endpoint", name),
                        Tag.of("statusCode", String.valueOf(statusCode))
                )
        );
    }

    public void increment(String name) {
        increment(name, Collections.emptySet());
    }

    public void increment(String name, Iterable<Tag> tags) {
        final Counter counter = counter(name, tags).register(meterRegistry);
        counter.increment();
    }

    public void count(String name, int amount) {
        count(name, Collections.emptySet(), amount);
    }

    public void count(String name, Iterable<Tag> tags, int amount) {
        final Counter counter = counter(name, tags).register(meterRegistry);
        counter.increment(1.0 * amount);
    }

    private Timer.Builder timer(String name) {
        return Timer.builder(name);
    }

    private Timer.Builder timer(String name, Iterable<Tag> tags) {
        return Timer.builder(name)
                .tags(tags);
    }

    private Counter.Builder counter(String name) {
        return Counter.builder(name);
    }

    private Counter.Builder counter(String name, Iterable<Tag> tags) {
        return Counter.builder(name)
                .tags(tags);
    }
}
