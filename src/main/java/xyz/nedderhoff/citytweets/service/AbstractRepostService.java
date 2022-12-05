package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.cache.RepostCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

public abstract class AbstractRepostService<
        IdType,
        AccountType extends Account,
        RepostCacheType extends RepostCache<IdType>,
        AccountServiceType extends AccountService<AccountType>
        >
        implements RepostService {

    protected final RepostCacheType repostCache;
    protected final AccountServiceType accountService;

    public AbstractRepostService(RepostCacheType repostCache, AccountServiceType accountService) {
        this.repostCache = repostCache;
        this.accountService = accountService;
    }

    protected boolean hasBeenSeen(IdType id) {
        return repostCache.contains(id);
    }

    protected boolean isAuthorBlocked(String username, AccountType account) {
        return account.ignoredAccounts().contains(username)
                || accountService.getIgnoredAccounts().contains(username);
    }

    protected void cache(IdType id) {
        repostCache.add(id);
    }

}
