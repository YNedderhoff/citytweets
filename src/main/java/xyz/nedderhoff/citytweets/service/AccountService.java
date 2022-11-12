package xyz.nedderhoff.citytweets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.config.TwitterAccountProperties;

import java.util.List;
import java.util.Random;

@Component
public class AccountService {
    private final List<TwitterAccount> accounts;

    @Autowired
    public AccountService(TwitterAccountProperties twitterAccountProperties) {
        this.accounts = twitterAccountProperties.getAccounts();
    }

    public List<TwitterAccount> getAccounts() {
        return accounts;
    }

    public String getRandomBearerToken() {
        int size = accounts.size();
        int randIdx = new Random().nextInt(size);

        TwitterAccount randomAccount = accounts.get(randIdx);

        return randomAccount.getAccountApiConfig().getBearerToken();
    }
}
