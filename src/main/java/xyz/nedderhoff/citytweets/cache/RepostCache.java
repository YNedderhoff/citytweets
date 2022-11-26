package xyz.nedderhoff.citytweets.cache;

public interface RepostCache<T> {

    boolean contains(T id);
    void add(T id);
}
