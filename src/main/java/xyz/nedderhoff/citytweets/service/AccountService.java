package xyz.nedderhoff.citytweets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;

import java.util.List;
import java.util.Random;

@Component
public class AccountService {
    private final List<Account> accounts;

    @Autowired
    public AccountService(AccountProperties accountProperties) {
        this.accounts = accountProperties.accounts();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getRandomBearerToken() {
        int size = accounts.size();
        int randIdx = new Random().nextInt(size);

        Account randomAccount = accounts.get(randIdx);

        return randomAccount.bearerToken();
    }
}
