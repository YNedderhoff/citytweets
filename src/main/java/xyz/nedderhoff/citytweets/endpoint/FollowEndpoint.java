package xyz.nedderhoff.citytweets.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1.TwitterFollowEndpoint;

@Component
public class FollowEndpoint {
    private final TwitterFollowEndpoint twitterFollowEndpoint;

    @Autowired
    public FollowEndpoint(TwitterFollowEndpoint twitterFollowEndpoint) {
        this.twitterFollowEndpoint = twitterFollowEndpoint;
    }

    public void follow(User user, TwitterAccount account) {
        //TODO distinguish User/Account by platform and call platform specific endpoint for it
        twitterFollowEndpoint.follow(user, account);
    }
}
