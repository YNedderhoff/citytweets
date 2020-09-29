package xyz.nedderhoff.citytweets.domain.http.recentsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentSearchMeta {
    private final long newestId;
    private final long oldestId;
    private final long resultCount;

    @JsonCreator
    public RecentSearchMeta(
            @JsonProperty("newest_id") long newestId,
            @JsonProperty("oldest_id") long oldestId,
            @JsonProperty("result_count") long resultCount
    ) {
        this.newestId = newestId;
        this.oldestId = oldestId;
        this.resultCount = resultCount;
    }

    public long getNewestId() {
        return newestId;
    }

    public long getOldestId() {
        return oldestId;
    }

    public long getResultCount() {
        return resultCount;
    }

    @Override
    public String toString() {
        return "RecentSearchMeta{" +
                "newestId='" + newestId + '\'' +
                ", oldestId='" + oldestId + '\'' +
                ", resultCount=" + resultCount +
                '}';
    }
}
