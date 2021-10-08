package xyz.nedderhoff.citytweets.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.RetweetCache;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.twitter.TwitterApi1Endpoint;

@Component
public class RetweetEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(RetweetEndpoint.class);
    private final Twitter twitter;
    private final RetweetCache retweetCache;

    @Autowired
    public RetweetEndpoint(
            Twitter twitter,
            RetweetCache retweetCache,
            RestTemplate rt
    ) {
        super(rt);
        this.retweetCache = retweetCache;
        this.twitter = twitter;
    }

    public void retweet(Tweet tweet) {
        final long id = tweet.id();
        logger.info("Retweeting tweet with id {} ...", id);
        try {
            twitter.retweetStatus(id);
            logger.info("Successfully retweeted tweet {}", id);
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
