package xyz.nedderhoff.citytweets.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.api2.TwitterUserEndpoint;

@Component
public class UserEndpoint {

    private final TwitterUserEndpoint twitterUserEndpoint;

    @Autowired
    public UserEndpoint(TwitterUserEndpoint twitterUserEndpoint) {
        this.twitterUserEndpoint = twitterUserEndpoint;
    }

    public User getById(long id) {
        //TODO distinguish User by platform and call platform specific endpoint for it
        return twitterUserEndpoint.getById(id);
    }

    public User getByName(String name) {
        //TODO distinguish User by platform and call platform specific endpoint for it
        return twitterUserEndpoint.getByName(name);
    }


}
