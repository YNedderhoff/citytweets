package xyz.nedderhoff.citytweets.cache;

public interface RepostCache<IdType> {

    boolean contains(IdType id);
    void add(IdType id);
}
