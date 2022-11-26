package xyz.nedderhoff.citytweets.service.twitter;

import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.List;
import java.util.Random;

@Component
public class TwitterAccountService implements AccountService<TwitterAccount> {
    private final List<TwitterAccount> twitterAccounts;

    public TwitterAccountService(AccountProperties accountProperties) {
        this.twitterAccounts = accountProperties.twitter();
    }

    public List<TwitterAccount> getAccounts() {
        return twitterAccounts;
    }

    public String getRandomBearerTokenTwitter() {
        int size = twitterAccounts.size();
        int randIdx = new Random().nextInt(size);

        TwitterAccount randomAccount = twitterAccounts.get(randIdx);

        return randomAccount.bearerToken();
    }
}
