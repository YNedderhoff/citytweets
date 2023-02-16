package xyz.nedderhoff.citytweets.cache;

import com.google.common.base.Stopwatch;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;
import xyz.nedderhoff.citytweets.monitoring.MetricService;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class AbstractFollowerCache<
        IdType,
        AccountType extends AccountProperties.Account,
        AccountServiceType extends AccountService<AccountType>,
        MetricServiceType extends MetricService,
        ExceptionType extends NonExistingCacheException
        > implements FollowerCache<IdType, AccountType, AccountServiceType, ExceptionType> {

    private static final int FOLLOWER_UPDATE_RATE = 1000 * 60 * 60 * 24;
    private static final int METRICS_REPORT_RATE = 1000 * 60;

    private final AccountServiceType accountService;
    private final Function<AccountType, Set<IdType>> friendsFetcher;
    private final MetricServiceType metricService;
    private final Map<AccountType, Set<IdType>> cache = new ConcurrentHashMap<>();
    private final Logger logger = getLogger();

    public AbstractFollowerCache(
            AccountServiceType accountService,
            Function<AccountType, Set<IdType>> friendsFetcher,
            MetricServiceType metricService) {
        this.accountService = accountService;
        this.friendsFetcher = friendsFetcher;
        this.metricService = metricService;

        this.logger.debug("Initialising ...");
        accountService.getAccounts().forEach(account -> {
            this.logger.debug("Preparing cache for account {}", account.name());
            cache.computeIfAbsent(account, a -> new HashSet<>());
        });
        this.logger.debug("Finished initialisation.");
    }

    // initial delay equals rate because the first population run happens in constructor
    @Scheduled(fixedRate = FOLLOWER_UPDATE_RATE)
    private void fetchFollowers() {
        logger.info("Running scheduled job in thread {}: fetchFollowers", Thread.currentThread().getName());
        populateCache();
    }

    @Override
    public boolean contains(IdType id, AccountType account) {
        return cache.get(account).contains(id);
    }

    @Override
    public void add(IdType id, AccountType account) {
        logger.info("Adding friend {} of account {}", id, account.name());
        cache.get(account).add(id);
    }

    private void populateCache() {
        final Stopwatch totalTimer = Stopwatch.createStarted();
        accountService.getAccounts().forEach(account -> {
            logger.debug("Populating cache for account {}", account.name());
            try {
                final Stopwatch timer = Stopwatch.createStarted();
                cache.get(account).addAll(friendsFetcher.apply(account));
                timer.stop();
                metricService.time(
                        "follower_cache_populate_time_per_account",
                        List.of(Tag.of("account", account.name())),
                        timer.elapsed(TimeUnit.MILLISECONDS)
                );
                logger.info("Cache updated for account {}, total size: {}", account.name(), cache.get(account).size());
            } catch (Exception e) {
                logger.error("Exception occurred while fetching friend list for account {}", account.name(), e);
            }
        });
        totalTimer.stop();
        metricService.time("follower_cache_populate_time_total", totalTimer.elapsed(TimeUnit.MILLISECONDS));
        this.logger.info("Warmed up in {}s", totalTimer.elapsed(TimeUnit.SECONDS));
    }

    protected abstract Logger getLogger();

    @Scheduled(fixedRate = METRICS_REPORT_RATE)
    private void reportCacheMetrics() {
        cache.forEach((key, value) -> metricService.count(
                "follower_cache_size",
                List.of(
                        Tag.of("account", key.name()),
                        Tag.of("service", accountService.getService().getName().toLowerCase())
                ),
                value.size())
        );

        metricService.count(
                "follower_cache_size_total",
                List.of(
                        Tag.of("service", accountService.getService().getName().toLowerCase())
                ),
                cache.values().stream()
                        .map(Set::size)
                        .mapToInt(Integer::intValue)
                        .sum()
        );
    }
}
