package xyz.nedderhoff.citytweets.cache;

import org.slf4j.Logger;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRepostCache<
        IdType,
        AccountType extends Account,
        AccountServiceType extends AccountService<AccountType>,
        ExceptionType extends NonExistingCacheException
        > implements RepostCache<IdType, AccountType, AccountServiceType, ExceptionType> {
    protected static final String CACHE_INEXISTENT_EXCEPTION_MESSAGE = "No cache exists for %s account %s";
    private final Map<AccountType, Set<IdType>> cache = new ConcurrentHashMap<>();
    private final Logger logger = getLogger();


    public AbstractRepostCache(AccountServiceType accountService) {
        this.logger.debug("Initialising ...");
        accountService.getAccounts().forEach(account -> {
            this.logger.debug("Preparing cache for account {}", account.name());
            cache.computeIfAbsent(account, a -> new HashSet<>());
        });
    }

    @Override
    public boolean contains(IdType id, AccountType account) {
        if (cache.containsKey(account)) {
            // TODO remove log
            logger.info("Cache contains account {}", account.name());
            final boolean contains = cache.get(account).contains(id);
            // TODO remove log
            logger.info("Cache contains status with id {}: {}", id, contains);
            return contains;
        } else {
            throw getException(getExceptionMessage(account));
        }
    }

    @Override
    public void add(IdType id, AccountType account) {
        logger.info("Adding post {}", id);

        cache.computeIfPresent(account, (a, reposts) -> {
            reposts.add(id);
            return reposts;
        });
        cache.computeIfAbsent(account, a -> new HashSet<>(List.of(id)));

        // TODO remove call
        contains(id, account);
    }

    protected abstract ExceptionType getException(String s);

    protected abstract String getExceptionMessage(AccountType account);

    protected abstract Logger getLogger();
}
