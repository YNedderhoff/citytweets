package xyz.nedderhoff.citytweets.service;

import org.slf4j.Logger;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

public abstract class AbstractFollowService<
        AccountType extends Account,
        AccountServiceType extends AccountService<AccountType>
        >
        implements FollowService {

    private final Logger logger = getLogger();

    protected final AccountServiceType accountService;

    public AbstractFollowService(AccountServiceType accountService) {
        this.accountService = accountService;
    }

    protected boolean isAuthorBlocked(String username, AccountType account) {
        if (accountService.getIgnoredAccounts().contains(username)) {
            logger.warn("Author {} is globally blocked in {}", username, account.getClass().getSimpleName());
            return true;
        }

        if (account.ignoredAccounts().contains(username)) {
            logger.warn("Author {} is blocked for {} {} ", username, account.getClass().getSimpleName(), account.name());
            return true;
        }
        return false;
    }

    protected abstract Logger getLogger();
}
