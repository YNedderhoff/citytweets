package xyz.nedderhoff.citytweets;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Component
@EnableScheduling
public class FollowerCache {
    private static final Logger logger = LoggerFactory.getLogger(FollowerCache.class);
    private static final int FRIEND_UPDATE_RATE = 1000 * 60 * 60 * 24;
    private static final Set<Long> cache = new ConcurrentSkipListSet<>();

    private final Twitter twitter;

    @Autowired
    public FollowerCache(Twitter twitter) {
        this.twitter = twitter;
    }

    @Scheduled(fixedRate = FRIEND_UPDATE_RATE)
    private void fetchFollowers() {
        try {
            long cursor = -1;
            boolean finished = false;
            Set<Long> friendIds = new HashSet<>();
            while (!finished) {
                IDs friends = twitter.getFriendsIDs(cursor);
                long[] ids = friends.getIDs();
                if (ids.length == 0) {
                    finished = true;
                } else {
                    cursor = friends.getNextCursor();
                    Arrays.stream(ids).forEach(friendIds::add);
                }
            }
            cache.addAll(friendIds);
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
