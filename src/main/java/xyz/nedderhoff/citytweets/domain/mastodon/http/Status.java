package xyz.nedderhoff.citytweets.domain.mastodon.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Status(
        @JsonDeserialize(using = FromStringDeserializer.class)
        Long id,
        String uri,
        String url,
        Account account,
        List<Mention> mentions,
        Reblog reblog,

        boolean reblogged
) {
    public record Mention(
            @JsonDeserialize(using = FromStringDeserializer.class)
            Long id,
            String username
    ) {
    }

    public record Reblog(
            @JsonDeserialize(using = FromStringDeserializer.class)
            Long id
    ) {

    }
}
