package xyz.nedderhoff.citytweets.api.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.api.twitter.TwitterApi1Endpoint;
import xyz.nedderhoff.citytweets.cache.twitter.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.TwitterAccount;

@Component
public class MeEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(MeEndpoint.class);

    @Autowired
    public MeEndpoint(
            RestTemplate rt,
            Twitter4jConnectionsCache connections
    ) {
        super(rt, connections);
    }

    public long getId(TwitterAccount account) throws TwitterException {
        logger.debug("Fetching own id for account {} ...", account.name());
        return connections.getConnection(account).getId();
    }
}