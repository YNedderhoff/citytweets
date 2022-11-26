package xyz.nedderhoff.citytweets.service;

import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;

import java.util.List;
import java.util.Random;

@Component
public class AccountService {
    private final List<TwitterAccount> twitterAccounts;
    private final List<AccountProperties.MastodonAccount> mastodonAccounts;

    public AccountService(AccountProperties accountProperties) {
        this.twitterAccounts = accountProperties.twitter();
        this.mastodonAccounts = accountProperties.mastodon();
    }

    public List<TwitterAccount> getTwitterAccounts() {
        return twitterAccounts;
    }

    public List<AccountProperties.MastodonAccount> getMastodonAccounts() {
        return mastodonAccounts;
    }

    public String getRandomBearerTokenTwitter() {
        int size = twitterAccounts.size();
        int randIdx = new Random().nextInt(size);

        TwitterAccount randomAccount = twitterAccounts.get(randIdx);

        return randomAccount.bearerToken();
    }
}
