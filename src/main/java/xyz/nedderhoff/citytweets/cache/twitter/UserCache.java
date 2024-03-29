package xyz.nedderhoff.citytweets.cache.twitter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.api.twitter.api2.UserEndpoint;
import xyz.nedderhoff.citytweets.domain.twitter.User;

import java.util.concurrent.TimeUnit;

@Lazy
@Component
// TODO implement for multiple accounts
public class UserCache {
    private static final Logger logger = LoggerFactory.getLogger(UserCache.class);
    private final LoadingCache<Long, User> cacheById;
    private final LoadingCache<String, User> cacheByName;

    public UserCache(UserEndpoint userEndpoint) {
        logger.debug("Initialising ...");
        cacheById = Caffeine
                .newBuilder()
                .expireAfterWrite(365, TimeUnit.DAYS)
                .recordStats()
                .build(userEndpoint::getById);

        cacheByName = Caffeine
                .newBuilder()
                .expireAfterWrite(365, TimeUnit.DAYS)
                .recordStats()
                .build(userEndpoint::getByName);
    }

    public User getById(long id) {
        logger.debug("Requesting user by id {} (current cache size {})", id, cacheById.estimatedSize());
        return cacheById.get(id);
    }

    public User getByName(String username) {
        logger.debug("Requesting user by name {} ((current cache size {})", username, cacheByName.estimatedSize());
        return cacheByName.get(username);
    }
}
