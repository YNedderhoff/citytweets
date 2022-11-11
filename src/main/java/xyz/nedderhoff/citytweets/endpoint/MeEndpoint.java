package xyz.nedderhoff.citytweets.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1.TwitterMeEndpoint;
import xyz.nedderhoff.citytweets.exceptions.PlatformException;

@Component
public class MeEndpoint {

    private final TwitterMeEndpoint twitterMeEndpoint;

    @Autowired
    public MeEndpoint(TwitterMeEndpoint twitterMeEndpoint) {
        this.twitterMeEndpoint = twitterMeEndpoint;
    }

    public long getId(Account account) throws PlatformException {
        //TODO distinguish Account by platform and call platform specific endpoint for it
        try {
            return twitterMeEndpoint.getId(account);
        } catch (TwitterException e) {
            throw new PlatformException(e);
        }
    }
}
