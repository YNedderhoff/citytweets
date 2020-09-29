package xyz.nedderhoff.citytweets.domain.http.recentsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentSearchData {
    private final long id;
    private final long authorId;
    private final String lang;
    private final String text;

    @JsonCreator
    public RecentSearchData(
            @JsonProperty("id") long id,
            @JsonProperty("author_id") long authorId,
            @JsonProperty("lang") String lang,
            @JsonProperty("text") String text
    ) {
        this.id = id;
        this.authorId = authorId;
        this.lang = lang;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public String getLang() {
        return lang;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "UserLookupData{" +
                "id='" + id + '\'' +
                ", authorId='" + authorId + '\'' +
                ", lang='" + lang + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
