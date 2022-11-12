package xyz.nedderhoff.citytweets.endpoint.platform.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.endpoint.platform.twitter.TwitterApi1Endpoint;

@Component
public class TwitterMeEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(TwitterMeEndpoint.class);

    @Autowired
    public TwitterMeEndpoint(
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
    }

    public long getId(TwitterAccount account) throws TwitterException {
        logger.debug("Fetching own id for account {} ...", account.getName());
        return connections.getConnection(account).getId();
    }
}
