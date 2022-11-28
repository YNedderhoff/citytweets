package xyz.nedderhoff.citytweets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.config.AccountProperties.PlatformProperties;

import java.util.List;

public abstract class AbstractAccountService<K extends Account, T extends PlatformProperties<K>>
        implements AccountService<K> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAccountService.class);

    protected final List<K> accounts;
    protected final List<String> ignoredAccounts;

    public AbstractAccountService(T properties) {
        this.accounts = properties.accounts();
        this.ignoredAccounts = properties.ignoredAccounts();

        logger.info("Found {} globally ignored {} accounts", ignoredAccounts.size(), properties.getClass().getSimpleName());
        accounts.forEach(account -> logger.info(
                "Found {} ignored accounts for {} {}",
                account.ignoredAccounts().size(),
                account.getClass().getSimpleName(),
                account.name()
        ));
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
