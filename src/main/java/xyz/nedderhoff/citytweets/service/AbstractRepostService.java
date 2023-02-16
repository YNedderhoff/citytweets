package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.cache.RepostCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

import java.util.function.Consumer;

public abstract class AbstractRepostService<
        AccountType extends Account,
        RepostCacheType extends RepostCache<AccountType, AccountServiceType, ?>,
        AccountServiceType extends AccountService<AccountType>
        >
        implements RepostService {

    protected final RepostCacheType repostCache;
    protected final AccountServiceType accountService;

    public AbstractRepostService(RepostCacheType repostCache, AccountServiceType accountService) {
        this.repostCache = repostCache;
        this.accountService = accountService;
    }

    protected boolean hasBeenSeen(Long id, AccountType account, Consumer<Long> hasBeenSeenLogger) {
        final boolean hasBeenSeen = repostCache.contains(id, account);
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

    protected void cache(Long id, AccountType account) {
        repostCache.add(id, account);
    }

}
