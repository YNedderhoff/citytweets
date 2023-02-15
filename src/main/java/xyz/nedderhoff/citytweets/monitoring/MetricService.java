package xyz.nedderhoff.citytweets.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MetricService {

    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;
    private final MeterRegistry meterRegistry;

    public MetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void timeTwitterEndpoint(String name, long t) {
        timeEndpoint(name, t, Service.TWITTER);
    }

    public void timeMastodonEndpoint(String name, long t) {
        timeEndpoint(name, t, Service.MASTODON);
    }

    public void timeEndpoint(String name, long t, Service service) {
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

    public void incrementTwitterEndpoint(String name, int statusCode) {
        incrementEndpoint(name, statusCode, Service.TWITTER);
    }

    public void incrementMastodonEndpoint(String name, int statusCode) {
        incrementEndpoint(name, statusCode, Service.MASTODON);
    }

    public void incrementEndpoint(String name, int statusCode, Service service) {
        increment(
                "api_error",
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
