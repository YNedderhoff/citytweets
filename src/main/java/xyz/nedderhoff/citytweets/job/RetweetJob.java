package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.RetweetCache;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.twitter.api1.RetweetEndpoint;
import xyz.nedderhoff.citytweets.twitter.api2.RecentTweetsEndpoint;

@Component
@EnableScheduling
public class RetweetJob {
    private static final Logger logger = LoggerFactory.getLogger(RetweetJob.class);
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final RetweetEndpoint retweetEndpoint;
    private final MeEndpoint meEndpoint;
    private final RetweetCache retweetCache;
    private final String search;

    @Autowired
    public RetweetJob(
            RecentTweetsEndpoint recentTweetsEndpoint,
            RetweetEndpoint retweetEndpoint, MeEndpoint meEndpoint, RetweetCache retweetCache,
            @Value("${search}") String search) {
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.retweetEndpoint = retweetEndpoint;
        this.meEndpoint = meEndpoint;
        this.retweetCache = retweetCache;
        this.search = search;
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void searchTweets() throws TwitterException {
        logger.info("Looking for unseen tweets for search {}", search);
        long myId = meEndpoint.getId();

        recentTweetsEndpoint.search(search).stream()
                .filter(tweet -> shouldRetweet(tweet, myId))
                .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                        tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                )
                .forEach(retweetEndpoint::retweet);
    }

    private boolean shouldRetweet(Tweet tweet, long myId) {
        return !isTweetFromMe(tweet, myId)
                && !isRetweet(tweet)
                && !hasBeenSeen(tweet);
    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isRetweet(Tweet tweet) {
        return tweet.text().startsWith("RT @");
    }

    private boolean hasBeenSeen(Tweet tweet) {
        return retweetCache.contains(tweet.id());
    }
}
