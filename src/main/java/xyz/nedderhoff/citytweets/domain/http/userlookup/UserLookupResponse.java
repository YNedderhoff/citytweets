package xyz.nedderhoff.citytweets.domain.http.userlookup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLookupResponse {
    private final UserLookupData data;

    @JsonCreator
    public UserLookupResponse(
            @JsonProperty("data") UserLookupData data
    ) {
        this.data = data;
    }

    public UserLookupData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "UserLookupResponse{" +
                "data=" + data +
                '}';
    }
}
