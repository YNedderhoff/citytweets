package xyz.nedderhoff.citytweets.domain.http.userlookup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLookupData {
    private final long id;
    private final String name;
    private final String username;
    private final String location;

    @JsonCreator
    public UserLookupData(
            @JsonProperty("id") long id,
            @JsonProperty("name") String name,
            @JsonProperty("username") String username,
            @JsonProperty("location") String location
    ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "UserLookupData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
