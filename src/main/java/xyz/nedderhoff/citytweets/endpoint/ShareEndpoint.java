package xyz.nedderhoff.citytweets.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1.TwitterRetweetEndpoint;

@Component
public class ShareEndpoint {

    private final TwitterRetweetEndpoint twitterRetweetEndpoint;

    @Autowired
    public ShareEndpoint(TwitterRetweetEndpoint twitterRetweetEndpoint) {
        this.twitterRetweetEndpoint = twitterRetweetEndpoint;
    }

    public void retweet(Tweet tweet, Account account) {
        //TODO distinguish Account by platform and call platform specific endpoint for it. Return abstract Post class of some kind.
        twitterRetweetEndpoint.retweet(tweet, account);
    }
}
