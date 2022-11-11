package xyz.nedderhoff.citytweets.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.api2.TwitterRecentTweetsEndpoint;

import java.util.List;

@Component
public class SearchEndpoint {

    private final TwitterRecentTweetsEndpoint twitterRecentTweetsEndpoint;

    @Autowired
    public SearchEndpoint(TwitterRecentTweetsEndpoint twitterRecentTweetsEndpoint) {
        this.twitterRecentTweetsEndpoint = twitterRecentTweetsEndpoint;
    }

    public List<Tweet> search(String query) {
        //TODO change Tweet to an abstract Post class of some kind
        return twitterRecentTweetsEndpoint.search(query);
    }
}
