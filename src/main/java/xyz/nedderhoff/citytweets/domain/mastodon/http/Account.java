package xyz.nedderhoff.citytweets.domain.mastodon.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Account(
        @JsonProperty("id") String id,
        @JsonProperty("username") String name,
        @JsonProperty("acct") String webfingerUri
) {
}
