package xyz.nedderhoff.citytweets.twitter.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.converter.RecentTweetsConverter;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.domain.http.recentsearch.RecentSearchResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecentTweetsEndpoint extends TwitterApi2Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(RecentTweetsEndpoint.class);
    public static final String BASE_QUERY_RECENT_TWEETS = BASE_TWITTER_API_2_URI + "tweets/search/recent?tweet.fields=author_id,lang&query=";

    private final RecentTweetsConverter recentTweetsConverter;
    private final HttpEntity<RecentSearchResponse> recentTweetsResponseEntity;

    @Autowired
    public RecentTweetsEndpoint(
            RecentTweetsConverter recentTweetsConverter,
            RestTemplate rt,
            @Value("${bearerToken}") String bearerToken
    ) {
        super(rt, bearerToken);
        this.recentTweetsConverter = recentTweetsConverter;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + this.bearerToken);

        recentTweetsResponseEntity = new HttpEntity<>(headers);
    }

    public List<Tweet> search(String query) {
        String uri = BASE_QUERY_RECENT_TWEETS + query;
        ResponseEntity<RecentSearchResponse> response = rt.exchange(uri, HttpMethod.GET, recentTweetsResponseEntity, RecentSearchResponse.class);

        List<Tweet> result = new ArrayList<>();
        if (response.getBody() != null && response.getBody().getData() != null) {
            result = recentTweetsConverter.toTweets(response.getBody().getData());
        }

        logger.info("Found {} tweets matching search {}", result.size(), query);
        return result;
    }
}
