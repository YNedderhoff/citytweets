package xyz.nedderhoff.citytweets.service.mastodon;

import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.config.AccountProperties;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.List;

@Service
public class MastodonAccountService implements AccountService<MastodonAccount> {
    private final List<MastodonAccount> mastodonAccounts;
    private final List<String> ignoredAccounts;

    public MastodonAccountService(AccountProperties accountProperties) {
        this.mastodonAccounts = accountProperties.mastodon().accounts();
        this.ignoredAccounts = accountProperties.mastodon().ignoredAccounts();
    }

    @Override
    public List<MastodonAccount> getAccounts() {
        return mastodonAccounts;
    }

    @Override
    public List<String> getIgnoredAccounts() {
        return ignoredAccounts;
    }
}
