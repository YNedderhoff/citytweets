package xyz.nedderhoff.citytweets.api.twitter.api2;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi2Endpoint;
import xyz.nedderhoff.citytweets.converter.RecentTweetsConverter;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.domain.twitter.http.recentsearch.RecentSearchResponse;
import xyz.nedderhoff.citytweets.monitoring.twitter.TwitterMetricService;
import xyz.nedderhoff.citytweets.service.twitter.TwitterAccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RecentTweetsEndpoint extends TwitterApi2Endpoint<RecentSearchResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RecentTweetsEndpoint.class);
    private static final String NAME = "search";
    private static final String BASE_QUERY_RECENT_TWEETS = BASE_TWITTER_API_2_URI + "tweets/search/recent?tweet.fields=author_id,lang&query=";

    private final RecentTweetsConverter recentTweetsConverter;
    private final HttpEntity<RecentSearchResponse> recentTweetsResponseEntity;

    public RecentTweetsEndpoint(
            RecentTweetsConverter recentTweetsConverter,
            RestTemplate rt,
            TwitterMetricService metricService,
            TwitterAccountService twitterAccountService
    ) {
        super(rt, metricService, twitterAccountService);
        this.recentTweetsConverter = recentTweetsConverter;
        this.recentTweetsResponseEntity = getResponseHttpEntity();
    }

    public List<Tweet> search(String query) {
        final String uri = BASE_QUERY_RECENT_TWEETS + query;

        Stopwatch timer = Stopwatch.createStarted();
        final ResponseEntity<RecentSearchResponse> response = rt.exchange(
                uri,
                HttpMethod.GET,
                recentTweetsResponseEntity,
                RecentSearchResponse.class
        );
        timer.stop();
        time(timer.elapsed(TimeUnit.MILLISECONDS));
        increment(response.getStatusCode().value());

        List<Tweet> result = new ArrayList<>();

        if (response.getBody() != null && response.getBody().data() != null) {
            result = recentTweetsConverter.toTweets(response.getBody().data());
        }

        logger.info("Found {} tweets matching search {}", result.size(), query);
        return result;
    }

    @Override
    public String name() {
        return NAME;
    }
}
