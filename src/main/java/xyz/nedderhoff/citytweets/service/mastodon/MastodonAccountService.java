package xyz.nedderhoff.citytweets.service.mastodon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonProperties;
import xyz.nedderhoff.citytweets.service.AbstractAccountService;

import static xyz.nedderhoff.citytweets.config.Service.MASTODON;

@Service
public class MastodonAccountService extends AbstractAccountService<MastodonAccount, MastodonProperties> {

    private static final Logger logger = LoggerFactory.getLogger(MastodonAccountService.class);

    public MastodonAccountService(AccountProperties accountProperties) {
        super(accountProperties.mastodon(), MASTODON);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
