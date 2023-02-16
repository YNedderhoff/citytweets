package xyz.nedderhoff.citytweets.monitoring.mastodon;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.Service;
import xyz.nedderhoff.citytweets.monitoring.MetricService;

import java.util.concurrent.TimeUnit;

@Component
public class MastodonMetricService extends MetricService {

    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    public MastodonMetricService(MeterRegistry meterRegistry) {
        super(meterRegistry, Service.MASTODON);
    }
}
