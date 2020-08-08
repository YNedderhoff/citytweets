package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.RetweetCache;
import xyz.nedderhoff.citytweets.twitter.TwitterService;

@Component
@EnableScheduling
public class RetweetJob {
    private static final Logger logger = LoggerFactory.getLogger(RetweetJob.class);
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final TwitterService twitter;
    private final RetweetCache retweetCache;
    private final Query query;

    @Autowired
    public RetweetJob(
            TwitterService twitter,
            RetweetCache retweetCache,
            @Value("${search}") String search) {
        this.twitter = twitter;
        this.retweetCache = retweetCache;

        this.query = new Query(search);
        this.query.setCount(100);
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void searchTweets() throws TwitterException {
        logger.info("Looking for unseen tweets for search {}", query.getQuery());
        long myId = twitter.getId();

        twitter.search(query).getTweets().stream()
                .filter(tweet -> shouldRetweet(tweet, myId))
                .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                        tweet.getId(), tweet.getUser().getName(), tweet.getLang(), tweet.getUser().getLocation(), tweet.getText())
                )
                .forEach(twitter::retweet);
    }

    private boolean shouldRetweet(Status tweet, long myId) {
        return !isTweetFromMe(tweet, myId)
                && !isRetweet(tweet)
                && !hasBeenSeen(tweet);
    }

    private boolean isTweetFromMe(Status tweet, long myId) {
        return tweet.getUser().getId() == myId;
    }

    private boolean isRetweet(Status tweet) {
        return tweet.getText().startsWith("RT @");
    }

    private boolean hasBeenSeen(Status tweet) {
        return retweetCache.contains(tweet.getId());
    }
}
