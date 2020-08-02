package xyz.nedderhoff.citytweets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Component
@EnableScheduling
public class RetweetJob {
    private static final Logger logger = LoggerFactory.getLogger(RetweetJob.class);
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final Twitter twitter;
    private final RetweetCache retweetCache;
    private final String search;

    @Autowired
    public RetweetJob(Twitter twitter,
                      RetweetCache retweetCache,
                      @Value("${search}") String search) {
        this.twitter = twitter;
        this.retweetCache = retweetCache;
        this.search = search;
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void searchTweets() throws TwitterException {
        logger.info("Looking for unseen tweets for search {}", search);

        Query query = new Query(search);
        query.setCount(100);
        QueryResult result = twitter.search(query);

        result.getTweets().stream()
                .filter(tweet -> !retweetCache.contains(tweet.getId()))
                .filter(tweet -> !tweet.getText().startsWith("RT @"))
                .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                        tweet.getId(), tweet.getUser().getName(), tweet.getLang(), tweet.getUser().getLocation(), tweet.getText())
                )
                .map(Status::getId)
                .forEach(this::retweet);
    }

    private void retweet(Long id) {
        logger.info("Retweeting tweet with id {} ...", id);
        try {
            twitter.retweetStatus(id);
            logger.info("Successfully retweetet tweet {}", id);
            retweetCache.add(id);
        } catch (TwitterException e) {
            if (e.getStatusCode() == 403 && e.getErrorCode() == 327) {
                logger.warn("Tweet {} has already been retweeted. Skipping and adding to cache.", id);
                retweetCache.add(id);
            } else {
                logger.error("Error trying to retweet tweet {}", id, e);
            }
        }
    }
}
