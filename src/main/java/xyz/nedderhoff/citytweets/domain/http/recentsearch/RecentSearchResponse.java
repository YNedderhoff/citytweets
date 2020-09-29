package xyz.nedderhoff.citytweets.domain.http.recentsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentSearchResponse {
    private final List<RecentSearchData> data;
    private final RecentSearchMeta meta;

    @JsonCreator
    public RecentSearchResponse(
            @JsonProperty("data") List<RecentSearchData> data,
            @JsonProperty("meta") RecentSearchMeta meta
    ) {
        this.data = data;
        this.meta = meta;
    }

    public RecentSearchMeta getMeta() {
        return meta;
    }

    public List<RecentSearchData> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RecentTweetsResponse{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }
}
