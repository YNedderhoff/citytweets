package xyz.nedderhoff.citytweets.domain.twitter;

public record User(
        long id,
        String name,
        String username,
        String location
) {
}
