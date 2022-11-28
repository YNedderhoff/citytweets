package xyz.nedderhoff.citytweets.api.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;
import xyz.nedderhoff.citytweets.exception.twitter.TwitterException;

@Component
public class MeEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(MeEndpoint.class);

    public MeEndpoint(
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
    }

    public long getId(TwitterAccount account) throws TwitterException {
        logger.debug("Fetching own id for account {} ...", account.name());
        try {
            return connections.getConnection(account).v1().users().verifyCredentials().getId();
        } catch (twitter4j.TwitterException e) {
            logger.error("Exception while fetching identity for account {}", account.name());
            throw new TwitterException("Exception while fetching identity for account " + account.name(), e);
        }
    }
}
