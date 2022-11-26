package xyz.nedderhoff.citytweets.domain.twitter;

public record Tweet(
        Long id,
        String lang,
        String text,
        User user
) {
}
