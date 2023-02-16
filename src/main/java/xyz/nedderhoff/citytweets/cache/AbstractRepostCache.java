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
        AccountType extends Account,
        AccountServiceType extends AccountService<AccountType>,
        ExceptionType extends NonExistingCacheException
        > implements RepostCache<AccountType, AccountServiceType, ExceptionType> {
    protected static final String CACHE_INEXISTENT_EXCEPTION_MESSAGE = "No cache exists for %s account %s";
    private final Map<AccountType, Set<Long>> cache = new ConcurrentHashMap<>();
    private final Logger logger = getLogger();


    public AbstractRepostCache(AccountServiceType accountService) {
        this.logger.debug("Initialising ...");
        accountService.getAccounts().forEach(account -> {
            this.logger.debug("Preparing cache for account {}", account.name());
            cache.computeIfAbsent(account, a -> new HashSet<>());
        });
    }

    @Override
    public boolean contains(Long id, AccountType account) {
        if (cache.containsKey(account)) {
            return cache.get(account).contains(id);
        } else {
            throw getException(getExceptionMessage(account));
        }
    }

    @Override
    public void add(Long id, AccountType account) {
        logger.info("Adding post {}", id);

        cache.computeIfPresent(account, (a, reposts) -> {
            reposts.add(id);
            return reposts;
        });
        cache.computeIfAbsent(account, a -> new HashSet<>(List.of(id)));
    }

    protected abstract ExceptionType getException(String s);

    protected abstract String getExceptionMessage(AccountType account);

    protected abstract Logger getLogger();
}
