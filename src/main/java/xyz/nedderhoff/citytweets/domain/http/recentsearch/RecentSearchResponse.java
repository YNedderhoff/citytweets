package xyz.nedderhoff.citytweets.domain.http.recentsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecentSearchResponse(
        @JsonProperty("data") List<RecentSearchData> data,
        @JsonProperty("meta") RecentSearchMeta meta
) {
}
