package xyz.nedderhoff.citytweets.domain.twitter.http.recentsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecentSearchMeta(
        @JsonProperty("newest_id") long newestId,
        @JsonProperty("oldest_id") long oldestId,
        @JsonProperty("result_count") long resultCount
) {
}
