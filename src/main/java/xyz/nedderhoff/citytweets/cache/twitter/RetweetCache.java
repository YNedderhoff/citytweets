package xyz.nedderhoff.citytweets.cache.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.cache.RepostCache;

import java.util.HashSet;
import java.util.Set;

@Lazy
@Component
public class RetweetCache implements RepostCache<Long> {
    private static final Logger logger = LoggerFactory.getLogger(RetweetCache.class);
    private static final Set<Long> cache = new HashSet<>();

    public RetweetCache() {
        logger.info("Setting up retweet cache");
    }

    @Override
    public boolean contains(Long id) {
        return cache.contains(id);
    }

    @Override
    public void add(Long id) {
        logger.info("Adding tweet {}", id);
        cache.add(id);
    }
}
