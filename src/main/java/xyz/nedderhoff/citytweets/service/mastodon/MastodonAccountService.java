package xyz.nedderhoff.citytweets.service.mastodon;

import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.List;

@Service
public class MastodonAccountService implements AccountService<MastodonAccount> {
    private final List<MastodonAccount> mastodonAccounts;

    public MastodonAccountService(AccountProperties accountProperties) {
        this.mastodonAccounts = accountProperties.mastodon();
    }

    @Override
    public List<MastodonAccount> getAccounts() {
        return mastodonAccounts;
    }
}
