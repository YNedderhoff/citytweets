package xyz.nedderhoff.citytweets.domain.http.userlookup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserLookupResponse(
        @JsonProperty("data") UserLookupData data
) {
}
