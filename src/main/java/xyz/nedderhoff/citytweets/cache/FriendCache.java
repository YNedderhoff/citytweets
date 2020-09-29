package xyz.nedderhoff.citytweets.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.twitter.api1.FriendsEndpoint;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
@EnableScheduling
public class FriendCache {
    private static final Logger logger = LoggerFactory.getLogger(FriendCache.class);
    private static final int FRIEND_UPDATE_RATE = 1000 * 60 * 60 * 24;
    private static final Set<Long> cache = new ConcurrentSkipListSet<>();

    private final FriendsEndpoint friendsEndpoint;

    @Autowired
    public FriendCache(FriendsEndpoint friendsEndpoint) {
        this.friendsEndpoint = friendsEndpoint;
    }

    @Scheduled(fixedRate = FRIEND_UPDATE_RATE)
    private void fetchFollowers() {
        logger.info("Populating Friends Cache");
        try {
            cache.addAll(friendsEndpoint.getFriends());
            logger.info("Cache updated, total size: {}", cache.size());
        } catch (TwitterException e) {
            logger.info("Exception occurred while fetching friend list", e);
        }
    }

    public boolean contains(Long id) {
        return cache.contains(id);
    }

    public void add(Long id) {
        logger.info("Adding friend {}", id);
        cache.add(id);
    }
}
