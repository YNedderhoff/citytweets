package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.cache.RepostCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

public abstract class AbstractRepostService<K, T extends RepostCache<K>, E extends Account, F extends AccountService<E>>
        implements RepostService {

    protected final T repostCache;
    protected final F accountService;

    public AbstractRepostService(T repostCache, F accountService) {
        this.repostCache = repostCache;
        this.accountService = accountService;
    }

    protected boolean hasBeenSeen(K id) {
        return repostCache.contains(id);
    }

    protected boolean isAuthorBlocked(String username, E account) {
        return account.ignoredAccounts().contains(username)
                || accountService.getIgnoredAccounts().contains(username);
    }

    protected void cache(K id) {
        repostCache.add(id);
    }

}
