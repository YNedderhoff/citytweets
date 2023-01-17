package xyz.nedderhoff.citytweets.cache.twitter;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.cache.AbstractRepostCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.config.Service;
import xyz.nedderhoff.citytweets.exception.twitter.NonExistingTwitterCacheException;
import xyz.nedderhoff.citytweets.service.twitter.TwitterAccountService;

@Lazy
@Component
public class RetweetCache extends AbstractRepostCache<
        Long,
        TwitterAccount,
        TwitterAccountService,
        NonExistingTwitterCacheException
        > {

    public RetweetCache(TwitterAccountService accountService) {
        super(Service.TWITTER, accountService);
    }

    @Override
    protected NonExistingTwitterCacheException getException(String s) {
        return new NonExistingTwitterCacheException(s);
    }

    @Override
    protected String getExceptionMessage(TwitterAccount account) {
        return String.format(CACHE_INEXISTENT_EXCEPTION_MESSAGE, "twitter", account.name());
    }
}
