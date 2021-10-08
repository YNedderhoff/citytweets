package xyz.nedderhoff.citytweets.domain.http.userlookup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserLookupData(
        @JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("username") String username,
        @JsonProperty("location") String location
) {
}
