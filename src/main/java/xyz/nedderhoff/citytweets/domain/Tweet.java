package xyz.nedderhoff.citytweets.domain;

public record Tweet(
        Long id,
        String lang,
        String text,
        User user
) {
}
