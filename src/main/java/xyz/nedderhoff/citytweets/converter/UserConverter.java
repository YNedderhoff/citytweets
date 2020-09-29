package xyz.nedderhoff.citytweets.converter;

import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.domain.http.userlookup.UserLookupData;

@Component
public class UserConverter {
    public User toUsers(UserLookupData data) {
        return new User(
                data.getId(),
                data.getName(),
                data.getUsername(),
                data.getLocation() == null ? "" : data.getLocation()
        );
    }
}
