package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.config.AccountProperties.PlatformProperties;

import java.util.List;

public abstract class AbstractAccountService<K extends Account, T extends PlatformProperties<K>>
        implements AccountService<K>{

    protected final List<K> accounts;
    protected final List<String> ignoredAccounts;

    public AbstractAccountService(T properties) {
        this.accounts = properties.accounts();
        this.ignoredAccounts = properties.ignoredAccounts();
    }

    @Override
    public List<K> getAccounts() {
        return this.accounts;
    }

    @Override
    public List<String> getIgnoredAccounts() {
        return this.ignoredAccounts;
    }
}
