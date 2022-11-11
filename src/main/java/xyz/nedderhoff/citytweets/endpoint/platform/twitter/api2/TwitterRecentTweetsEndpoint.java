package xyz.nedderhoff.citytweets.endpoint.platform.twitter.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.converter.RecentTweetsConverter;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.domain.http.recentsearch.RecentSearchResponse;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.TwitterApi2Endpoint;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.ArrayList;
import java.util.List;

@Component
public class TwitterRecentTweetsEndpoint extends TwitterApi2Endpoint<RecentSearchResponse> {
    private static final Logger logger = LoggerFactory.getLogger(TwitterRecentTweetsEndpoint.class);
    private static final String BASE_QUERY_RECENT_TWEETS = BASE_TWITTER_API_2_URI + "tweets/search/recent?tweet.fields=author_id,lang&query=";

    private final RecentTweetsConverter recentTweetsConverter;
    private final HttpEntity<RecentSearchResponse> recentTweetsResponseEntity;

    @Autowired
    public TwitterRecentTweetsEndpoint(
            RecentTweetsConverter recentTweetsConverter,
            RestTemplate rt,
            AccountService accountService
    ) {
        super(rt, accountService);
        this.recentTweetsConverter = recentTweetsConverter;
        this.recentTweetsResponseEntity = getResponseHttpEntity();
    }

    public List<Tweet> search(String query) {
        final String uri = BASE_QUERY_RECENT_TWEETS + query;
        final ResponseEntity<RecentSearchResponse> response = rt.exchange(
                uri,
                HttpMethod.GET,
                recentTweetsResponseEntity,
                RecentSearchResponse.class
        );

        List<Tweet> result = new ArrayList<>();
        if (response.getBody() != null && response.getBody().data() != null) {
            result = recentTweetsConverter.toTweets(response.getBody().data());
        }

        logger.info("Found {} tweets matching search {}", result.size(), query);
        return result;
    }
}
