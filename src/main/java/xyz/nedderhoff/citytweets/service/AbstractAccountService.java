package xyz.nedderhoff.citytweets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.config.AccountProperties.PlatformProperties;
import xyz.nedderhoff.citytweets.config.Service;

import java.util.List;

public abstract class AbstractAccountService<
        AccountType extends Account,
        PlatformPropertiesType extends PlatformProperties<AccountType>
        >
        implements AccountService<AccountType> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAccountService.class);

    protected final List<AccountType> accounts;
    protected final List<String> ignoredAccounts;
    private final Service service;

    public AbstractAccountService(PlatformPropertiesType properties, Service service) {
        this.accounts = properties.accounts();
        this.ignoredAccounts = properties.ignoredAccounts();
        this.service = service;

        logger.info("Found {} globally ignored {} accounts", ignoredAccounts.size(), properties.getClass().getSimpleName());
        accounts.forEach(account -> logger.info(
                "Found {} ignored accounts for {} {}",
                account.ignoredAccounts().size(),
                account.getClass().getSimpleName(),
                account.name()
        ));
    }

    @Override
    public List<AccountType> getAccounts() {
        return this.accounts;
    }

    @Override
    public List<String> getIgnoredAccounts() {
        return this.ignoredAccounts;
    }

    @Override
    public Service getService(){
        return service;
    }
}
