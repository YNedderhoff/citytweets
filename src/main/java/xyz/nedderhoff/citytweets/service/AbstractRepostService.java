package xyz.nedderhoff.citytweets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nedderhoff.citytweets.cache.RepostCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

import java.util.function.Consumer;

public abstract class AbstractRepostService<
        IdType,
        AccountType extends Account,
        RepostCacheType extends RepostCache<IdType>,
        AccountServiceType extends AccountService<AccountType>
        >
        implements RepostService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRepostService.class);

    protected final RepostCacheType repostCache;
    protected final AccountServiceType accountService;

    public AbstractRepostService(RepostCacheType repostCache, AccountServiceType accountService) {
        this.repostCache = repostCache;
        this.accountService = accountService;
    }

    protected boolean hasBeenSeen(IdType id, Consumer<IdType> hasBeenSeenLogger) {
        final boolean hasBeenSeen = repostCache.contains(id);
        if (hasBeenSeen) {
            hasBeenSeenLogger.accept(id);

        }
        return hasBeenSeen;
    }

    protected boolean isAuthorBlocked(String username, AccountType account, Consumer<String> authorBlockedLogger) {
        final boolean isAuthorBlocked = account.ignoredAccounts().contains(username)
                || accountService.getIgnoredAccounts().contains(username);
        if (isAuthorBlocked) {
            authorBlockedLogger.accept(username);
        }
        return isAuthorBlocked;
    }

    protected void cache(IdType id) {
        repostCache.add(id);
    }

}
