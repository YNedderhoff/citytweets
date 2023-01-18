package xyz.nedderhoff.citytweets.cache.mastodon;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.cache.AbstractRepostCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.config.Service;
import xyz.nedderhoff.citytweets.exception.mastodon.NonExistingMastodonCacheException;
import xyz.nedderhoff.citytweets.service.mastodon.MastodonAccountService;

@Lazy
@Component
public class RetootCache extends AbstractRepostCache<
        String,
        MastodonAccount,
        MastodonAccountService,
        NonExistingMastodonCacheException
        > {

    public RetootCache(MastodonAccountService accountService) {
        super(Service.MASTODON, accountService);
    }

    @Override
    protected NonExistingMastodonCacheException getException(String s) {
        return new NonExistingMastodonCacheException(s);
    }

    @Override
    protected String getExceptionMessage(MastodonAccount account) {
        return String.format(CACHE_INEXISTENT_EXCEPTION_MESSAGE, "twitter", account.name());
    }
}
