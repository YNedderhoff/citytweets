package xyz.nedderhoff.citytweets.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.endpoint.FriendsEndpoint;
import xyz.nedderhoff.citytweets.exceptions.PlatformException;
import xyz.nedderhoff.citytweets.service.AccountService;

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
    private final AccountService accountService;

    @Autowired
    public FriendCache(FriendsEndpoint friendsEndpoint, AccountService accountService) {
        this.friendsEndpoint = friendsEndpoint;
        this.accountService = accountService;
    }

    @Scheduled(fixedRate = FRIEND_UPDATE_RATE)
    private void fetchFollowers() {
        accountService.getAccounts().forEach(account -> {
            logger.info("Populating Friends Cache for account {}", account.getName());
            try {
                cache.put(account.getName(), friendsEndpoint.getFriends(account));
                logger.info("Cache updated for account {}, total size: {}", account.getName(), cache.get(account.getName()).size());
            } catch (PlatformException e) {
                logger.error("Exception occurred while fetching friend list for account {}", account.getName(), e);
            }
        });
    }

    public boolean contains(Long id, TwitterAccount account) {
        return cache.get(account.getName()).contains(id);
    }

    public void add(Long id, TwitterAccount account) {
        logger.info("Adding friend {} to account {}", id, account.getName());
        cache.get(account.getName()).add(id);
    }
}
