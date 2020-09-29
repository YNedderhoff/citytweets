package xyz.nedderhoff.citytweets.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Component
public class MeEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(MeEndpoint.class);

    private final Twitter twitter;

    @Autowired
    public MeEndpoint(
            Twitter twitter,
            RestTemplate rt
    ) {
        super(rt);
        this.twitter = twitter;
    }

    public long getId() throws TwitterException {
        logger.info("Fetching own id ...");
        return twitter.getId();
    }
}
