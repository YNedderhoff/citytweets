package xyz.nedderhoff.citytweets.converter;

import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.domain.twitter.User;
import xyz.nedderhoff.citytweets.domain.twitter.http.userlookup.UserLookupData;

@Component
public class UserConverter {
    public User toUsers(UserLookupData data) {
        return new User(
                data.id(),
                data.name(),
                data.username(),
                data.location() == null ? "" : data.location()
        );
    }
}
