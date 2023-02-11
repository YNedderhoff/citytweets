package xyz.nedderhoff.citytweets.cache.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.api.twitter.api1.FriendsEndpoint;
import xyz.nedderhoff.citytweets.cache.AbstractFollowerCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.exception.twitter.NonExistingTwitterCacheException;
import xyz.nedderhoff.citytweets.service.twitter.TwitterAccountService;

@Lazy
@Component
@EnableScheduling
public class TwitterFollowerCache extends AbstractFollowerCache<
        Long,
        TwitterAccount,
        TwitterAccountService,
        NonExistingTwitterCacheException
        > {
private static final Logger logger = LoggerFactory.getLogger(TwitterFollowerCache.class);

    public TwitterFollowerCache(FriendsEndpoint friendsEndpoint, TwitterAccountService twitterAccountService) {
        super(twitterAccountService, friendsEndpoint::getFriends);
    }
}
