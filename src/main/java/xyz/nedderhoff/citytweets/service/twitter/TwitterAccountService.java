package xyz.nedderhoff.citytweets.service.twitter;

import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterProperties;
import xyz.nedderhoff.citytweets.service.AbstractAccountService;

import java.util.Random;

@Service
public class TwitterAccountService extends AbstractAccountService<TwitterAccount, TwitterProperties> {

    public TwitterAccountService(AccountProperties accountProperties) {
        super(accountProperties.twitter());
    }

    public String getRandomBearerTokenTwitter() {
        int size = this.accounts.size();
        int randIdx = new Random().nextInt(size);

        TwitterAccount randomAccount = this.accounts.get(randIdx);

        return randomAccount.bearerToken();
    }
}
