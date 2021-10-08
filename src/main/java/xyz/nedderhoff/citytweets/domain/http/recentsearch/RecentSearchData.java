package xyz.nedderhoff.citytweets.domain.http.recentsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecentSearchData(
        @JsonProperty("id") long id,
        @JsonProperty("author_id") long authorId,
        @JsonProperty("lang") String lang,
        @JsonProperty("text") String text
) {
}
