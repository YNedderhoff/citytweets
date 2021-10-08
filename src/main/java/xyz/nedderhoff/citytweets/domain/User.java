package xyz.nedderhoff.citytweets.domain;

public record User(
        long id,
        String name,
        String username,
        String location
) {
}
