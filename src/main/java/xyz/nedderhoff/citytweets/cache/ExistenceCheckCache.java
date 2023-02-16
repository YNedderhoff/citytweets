package xyz.nedderhoff.citytweets.cache;

import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;
import xyz.nedderhoff.citytweets.service.AccountService;

public interface ExistenceCheckCache<
        AccountType extends Account,
        AccountServiceType extends AccountService<AccountType>,
        ExceptionType extends NonExistingCacheException
        > {

    boolean contains(Long id, AccountType account);

    void add(Long id, AccountType account);
}
