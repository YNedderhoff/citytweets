package xyz.nedderhoff.citytweets.cache;

import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;
import xyz.nedderhoff.citytweets.service.AccountService;

public interface FollowerCache<
        IdType,
        AccountType extends AccountProperties.Account,
        AccountServiceType extends AccountService<AccountType>,
        ExceptionType extends NonExistingCacheException
        > extends ExistenceCheckCache<IdType, AccountType, AccountServiceType, ExceptionType> {
}
