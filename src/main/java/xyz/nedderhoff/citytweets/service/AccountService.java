package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

import java.util.List;

public interface AccountService<AccountType extends Account> {
    List<AccountType> getAccounts();

    List<String> getIgnoredAccounts();
}
