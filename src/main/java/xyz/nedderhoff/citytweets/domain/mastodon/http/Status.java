package xyz.nedderhoff.citytweets.domain.mastodon.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Status(
        String id,
        String uri,
        String url,
        Account account,
        List<Mention> mentions
) {
    public record Mention(
            String id,
            String username
    ) {
    }
}
