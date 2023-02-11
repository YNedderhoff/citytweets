package xyz.nedderhoff.citytweets.service.mastodon;

import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonProperties;
import xyz.nedderhoff.citytweets.service.AbstractAccountService;

import static xyz.nedderhoff.citytweets.config.Service.MASTODON;

@Service
public class MastodonAccountService extends AbstractAccountService<MastodonAccount, MastodonProperties> {

    public MastodonAccountService(AccountProperties accountProperties) {
        super(accountProperties.mastodon(), MASTODON);
    }
}
