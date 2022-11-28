package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

public abstract class AbstractFollowService<K extends Account, T extends AccountService<K>>
        implements FollowService {

    protected final T accountService;

    public AbstractFollowService(T accountService) {
        this.accountService = accountService;
    }

    protected boolean isAuthorBlocked(String username, K account) {
        return account.ignoredAccounts().contains(username)
                || accountService.getIgnoredAccounts().contains(username);
    }
}
