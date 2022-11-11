package xyz.nedderhoff.citytweets.platform.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.RetweetCache;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.platform.twitter.TwitterApi1Endpoint;

import static xyz.nedderhoff.citytweets.config.AccountProperties.Account;

@Component
public class RetweetEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(RetweetEndpoint.class);
    private final RetweetCache retweetCache;

    @Autowired
    public RetweetEndpoint(
            RetweetCache retweetCache,
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
        this.retweetCache = retweetCache;
    }

    public void retweet(Tweet tweet, Account account) {
        final long id = tweet.id();
        logger.info("Retweeting tweet with id {} ...", id);
        try {
            connections.getConnection(account).retweetStatus(id);
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
