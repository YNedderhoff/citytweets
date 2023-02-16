package xyz.nedderhoff.citytweets.domain.mastodon.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import xyz.nedderhoff.citytweets.util.serialization.LongFromStringDeserializer;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Status(
        @JsonDeserialize(using = LongFromStringDeserializer.class)
        Long id,
        String uri,
        String url,
        Account account,
        List<Mention> mentions,
        Reblog reblog,

        boolean reblogged
) {
    public record Mention(
            @JsonDeserialize(using = LongFromStringDeserializer.class)
            Long id,
            String username
    ) {
    }

    public record Reblog(
            @JsonDeserialize(using = LongFromStringDeserializer.class)
            Long id
    ) {

    }
}
