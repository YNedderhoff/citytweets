package xyz.nedderhoff.citytweets.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.twitter.TwitterApi1Endpoint;

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

    public long getId(Account account) throws TwitterException {
        logger.debug("Fetching own id for account {} ...", account.name());
        return connections.getConnection(account).getId();
    }
}
