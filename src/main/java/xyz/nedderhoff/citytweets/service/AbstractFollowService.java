package xyz.nedderhoff.citytweets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

public abstract class AbstractFollowService<K extends Account, T extends AccountService<K>>
        implements FollowService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFollowService.class);

    protected final T accountService;

    public AbstractFollowService(T accountService) {
        this.accountService = accountService;
    }

    protected boolean isAuthorBlocked(String username, K account) {
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
}
