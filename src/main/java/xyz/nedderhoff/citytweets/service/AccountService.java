package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

import java.util.List;

public interface AccountService<T extends Account> {
    List<T> getAccounts();
}
