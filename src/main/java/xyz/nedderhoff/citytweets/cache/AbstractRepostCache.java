package xyz.nedderhoff.citytweets.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.config.Service;
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
        > implements RepostCache<IdType, AccountType> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRepostCache.class);
    protected static final String CACHE_INEXISTENT_EXCEPTION_MESSAGE = "No cache exists for %s account %s";
    private final Map<AccountType, Set<IdType>> cache = new ConcurrentHashMap<>();


    public AbstractRepostCache(Service type, AccountServiceType accountService) {
        logger.info("Setting up repost cache for {}", type.getType());
        accountService.getAccounts().forEach(account -> {
            logger.info("Preparing {} repost cache for account {}", type.getType(), account.name());
            cache.computeIfAbsent(account, a -> new HashSet<>());
        });
    }

    @Override
    public boolean contains(IdType id, AccountType account) {
        if (cache.containsKey(account)) {
            return cache.get(account).contains(id);
        } else {
            throw getException(getExceptionMessage(account));
        }
    }

    @Override
    public void add(IdType id, AccountType account) {
        logger.info("Adding tweet {}", id);

        cache.computeIfPresent(account, (t, s) -> {
            s.add(id);
            return s;
        });
        cache.computeIfAbsent(account, t -> new HashSet<>(List.of(id)));
    }

    protected abstract ExceptionType getException(String s);

    protected abstract String getExceptionMessage(AccountType account);
}
