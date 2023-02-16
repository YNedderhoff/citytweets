package xyz.nedderhoff.citytweets.domain.mastodon.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Account(
        @JsonDeserialize(using = FromStringDeserializer.class)
        @JsonProperty("id") Long id,
        @JsonProperty("username") String name,
        @JsonProperty("acct") String webfingerUri
) {
}
