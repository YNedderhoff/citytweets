package xyz.nedderhoff.citytweets.monitoring;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MetricService {

    public void time(String name, long t) {
        time(name, Collections.emptySet(), t);
    }

    public void time(String name, Iterable<Tag> tags, long t) {

    }

    private Timer.Builder timer(String name) {
        return Timer.builder(name);
    }
}
