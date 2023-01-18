package xyz.nedderhoff.citytweets.cache;

import xyz.nedderhoff.citytweets.config.AccountProperties;

public interface RepostCache<IdType, AccountType extends AccountProperties.Account> {

    boolean contains(IdType id, AccountType account);

    void add(IdType id, AccountType accountType);
}
