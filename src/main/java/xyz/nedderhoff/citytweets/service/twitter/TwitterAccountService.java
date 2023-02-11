package xyz.nedderhoff.citytweets.service.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterProperties;
import xyz.nedderhoff.citytweets.service.AbstractAccountService;

import java.util.Random;

import static xyz.nedderhoff.citytweets.config.Service.TWITTER;

@Service
public class TwitterAccountService extends AbstractAccountService<TwitterAccount, TwitterProperties> {

    private static final Logger logger = LoggerFactory.getLogger(TwitterAccountService.class);
    public TwitterAccountService(AccountProperties accountProperties) {
        super(accountProperties.twitter(), TWITTER);
    }

    public String getRandomBearerTokenTwitter() {
        int size = this.accounts.size();
        int randIdx = new Random().nextInt(size);

        TwitterAccount randomAccount = this.accounts.get(randIdx);

        return randomAccount.bearerToken();
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
