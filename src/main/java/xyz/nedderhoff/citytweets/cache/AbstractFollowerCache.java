package xyz.nedderhoff.citytweets.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.nedderhoff.citytweets.cache.twitter.TwitterFollowerCache;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class AbstractFollowerCache<
        IdType,
        AccountType extends AccountProperties.Account,
        AccountServiceType extends AccountService<AccountType>,
        ExceptionType extends NonExistingCacheException
        > implements FollowerCache<IdType, AccountType, AccountServiceType, ExceptionType> {

    private static final Logger logger = LoggerFactory.getLogger(TwitterFollowerCache.class);
    private static final int FOLLOWER_UPDATE_RATE = 1000 * 60 * 60 * 24;
    
    private final AccountServiceType accountService;
    private final Function<AccountType, Set<IdType>> friendsFetcher;
    private final Map<AccountType, Set<IdType>> cache = new ConcurrentHashMap<>();

    public AbstractFollowerCache(
            AccountServiceType accountService,
            Function<AccountType, Set<IdType>> friendsFetcher
    ) {
        this.accountService = accountService;
        this.friendsFetcher = friendsFetcher;

        logger.info("Initialising ...");
        accountService.getAccounts().forEach(account -> {
            logger.info("Preparing cache for account {}", account.name());
            cache.computeIfAbsent(account, a -> new HashSet<>());
        });

        logger.info("Warming up ...");
        populateCache();
    }

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
        logger.info("Adding friend {} to account {}", id, account.name());
        cache.get(account).add(id);
    }

    private void populateCache(){
        accountService.getAccounts().forEach(account -> {
            logger.info("Populating cache for account {}", account.name());
            try {
                cache.get(account).addAll(friendsFetcher.apply(account));
                logger.info("Cache updated for account {}, total size: {}", account.name(), cache.get(account).size());
            } catch (Exception e) {
                logger.error("Exception occurred while fetching friend list for account {}", account.name(), e);
            }
        });
    }
}
