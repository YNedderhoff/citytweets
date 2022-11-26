package xyz.nedderhoff.citytweets.cache.mastodon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.cache.RepostCache;

import java.util.HashSet;
import java.util.Set;

@Component
public class RetootCache implements RepostCache<String> {
    private static final Logger logger = LoggerFactory.getLogger(RetootCache.class);
    private static final Set<String> cache = new HashSet<>();

    public boolean contains(String id) {
        return cache.contains(id);
    }

    public void add(String id) {
        logger.info("Adding toot {}", id);
        cache.add(id);
    }
}
