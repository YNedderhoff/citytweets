package xyz.nedderhoff.citytweets.twitter.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.twitter.TwitterApi1Endpoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class FriendsEndpoint extends TwitterApi1Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(FriendsEndpoint.class);
    private final Twitter twitter;

    @Autowired
    public FriendsEndpoint(
            RestTemplate rt,
            Twitter twitter
    ) {
        super(rt);
        this.twitter = twitter;
    }

    public Set<Long> getFriends() throws TwitterException {
        logger.info("Fetching friends ...");
        long cursor = -1;
        boolean finished = false;
        Set<Long> friendIds = new HashSet<>();
        while (!finished) {
            logger.info("Doing iteration with cursor {}", cursor);
            IDs friends = twitter.getFriendsIDs(cursor);
            long[] ids = friends.getIDs();
            if (ids.length == 0) {
                logger.info("Full friends list fetched, total size: {}", friendIds.size());
                finished = true;
            } else {
                cursor = friends.getNextCursor();
                Arrays.stream(ids).forEach(friendIds::add);
            }
        }
        return friendIds;
    }
}
