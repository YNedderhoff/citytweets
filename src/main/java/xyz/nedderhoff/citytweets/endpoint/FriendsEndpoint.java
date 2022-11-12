package xyz.nedderhoff.citytweets.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1.TwitterFriendsEndpoint;
import xyz.nedderhoff.citytweets.exceptions.PlatformException;

import java.util.Set;

@Component
public class FriendsEndpoint {

    private final TwitterFriendsEndpoint twitterFriendsEndpoint;

    @Autowired
    public FriendsEndpoint(TwitterFriendsEndpoint twitterFriendsEndpoint) {
        this.twitterFriendsEndpoint = twitterFriendsEndpoint;
    }

    public Set<Long> getFriends(TwitterAccount account) throws PlatformException {
        //TODO distinguish Account by platform and call platform specific endpoint for it
        try {
            return twitterFriendsEndpoint.getFriends(account);
        } catch (TwitterException e) {
            throw new PlatformException("Error when talking to Twitter API", e);
        }
    }
}
