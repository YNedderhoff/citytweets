package xyz.nedderhoff.citytweets.api;

public interface HttpEndpoint {

    void time(long t);
    void increment(int statusCode);

    String name();
}
