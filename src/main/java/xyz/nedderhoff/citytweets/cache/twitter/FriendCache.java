package xyz.nedderhoff.citytweets.cache.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.api.twitter.api1.FriendsEndpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.service.impl.TwitterAccountService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
@EnableScheduling
public class FriendCache {
    private static final Logger logger = LoggerFactory.getLogger(FriendCache.class);
    private static final int FRIEND_UPDATE_RATE = 1000 * 60 * 60 * 24;
    private static final Map<String, Set<Long>> cache = new ConcurrentSkipListMap<>();

    private final FriendsEndpoint friendsEndpoint;
    private final TwitterAccountService twitterAccountService;

    public FriendCache(FriendsEndpoint friendsEndpoint, TwitterAccountService twitterAccountService) {
        this.friendsEndpoint = friendsEndpoint;
        this.twitterAccountService = twitterAccountService;
    }

    @Scheduled(fixedRate = FRIEND_UPDATE_RATE)
    private void fetchFollowers() {
        twitterAccountService.getAccounts().forEach(account -> {
            logger.info("Populating Friends Cache for account {}", account.name());
            try {
                cache.put(account.name(), friendsEndpoint.getFriends(account));
                logger.info("Cache updated for account {}, total size: {}", account.name(), cache.get(account.name()).size());
            } catch (TwitterException e) {
                logger.error("Exception occurred while fetching friend list for account {}", account.name(), e);
            }
        });
    }

    public boolean contains(Long id, TwitterAccount account) {
        return cache.get(account.name()).contains(id);
    }

    public void add(Long id, TwitterAccount account) {
        logger.info("Adding friend {} to account {}", id, account.name());
        cache.get(account.name()).add(id);
    }
}
