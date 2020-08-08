package xyz.nedderhoff.citytweets.cache;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetweetCache {
    private static final Logger logger = LoggerFactory.getLogger(RetweetCache.class);
    private static final Set<Long> cache = new HashSet<>();

    public boolean contains(Long id) {
        return cache.contains(id);
    }

    public void add(Long id) {
        logger.info("Adding tweet {}", id);
        cache.add(id);
    }
}
