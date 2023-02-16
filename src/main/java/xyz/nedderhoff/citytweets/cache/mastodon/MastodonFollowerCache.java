package xyz.nedderhoff.citytweets.cache.mastodon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.api.mastodon.api1.AccountsEndpoint;
import xyz.nedderhoff.citytweets.cache.AbstractFollowerCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.exception.twitter.NonExistingTwitterCacheException;
import xyz.nedderhoff.citytweets.monitoring.mastodon.MastodonMetricService;
import xyz.nedderhoff.citytweets.service.mastodon.MastodonAccountService;

@Lazy
@Component
@EnableScheduling
public class MastodonFollowerCache extends AbstractFollowerCache<
        String,
        MastodonAccount,
        MastodonAccountService,
        MastodonMetricService,
        NonExistingTwitterCacheException
        > {
    private static final Logger logger = LoggerFactory.getLogger(MastodonFollowerCache.class);

    public MastodonFollowerCache(
            AccountsEndpoint accountsEndpoint,
            MastodonAccountService accountService,
            MastodonMetricService metricService
    ) {
        super(accountService, accountsEndpoint::getFollowers, metricService);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
