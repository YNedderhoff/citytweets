package xyz.nedderhoff.citytweets.service;

import xyz.nedderhoff.citytweets.cache.RepostCache;

public abstract class AbstractRepostService<K, T extends RepostCache<K>> implements RepostService {

    final T repostCache;

    public AbstractRepostService(T repostCache) {
        this.repostCache = repostCache;
    }

    protected boolean hasBeenSeen(K id) {
        return repostCache.contains(id);
    }

    protected void cache(K id) {
        repostCache.add(id);
    }

}
