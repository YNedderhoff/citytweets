package xyz.nedderhoff.citytweets.twitter.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.Twitter4jConnectionsCache;
import xyz.nedderhoff.citytweets.converter.UserConverter;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.domain.http.userlookup.UserLookupResponse;
import xyz.nedderhoff.citytweets.service.AccountService;
import xyz.nedderhoff.citytweets.twitter.TwitterApi2Endpoint;

@Component
public class UserEndpoint extends TwitterApi2Endpoint<UserLookupResponse> {
    private static final Logger logger = LoggerFactory.getLogger(UserEndpoint.class);
    public static final String BASE_QUERY_USER_BY_ID = BASE_TWITTER_API_2_URI + "users/%d?user.fields=location";
    public static final String BASE_QUERY_USER_BY_NAME = BASE_TWITTER_API_2_URI + "users/by/username/%s?user.fields=location";

    private final Twitter4jConnectionsCache connections;
    private final UserConverter userConverter;
    private final HttpEntity<UserLookupResponse> userResponseEntity;

    @Autowired
    public UserEndpoint(
            Twitter twitter,
            RestTemplate rt,
            Twitter4jConnectionsCache connections,
            AccountService accountService,
            UserConverter userConverter
    ) {
        super(rt, accountService);
        this.connections = connections;
        this.userConverter = userConverter;
        this.userResponseEntity = getResponseHttpEntity();
    }

    public long getId() throws TwitterException {
        return connections.getRandomConnection().getId();
    }

    public User getById(long id) {
        logger.debug("Requesting user by id {}", id);

        final String uri = String.format(BASE_QUERY_USER_BY_ID, id);
        final ResponseEntity<UserLookupResponse> response = rt.exchange(
                uri,
                HttpMethod.GET,
                userResponseEntity,
                UserLookupResponse.class
        );

        User result = null;
        if (response.getBody() != null && response.getBody().data() != null) {
            result = userConverter.toUsers(response.getBody().data());
        } else {
            logger.warn("No user found with id {}", id);
        }
        return result;
    }

    public User getByName(String name) {
        logger.debug("Requesting user by name {}", name);

        final String uri = String.format(BASE_QUERY_USER_BY_NAME, name);
        final ResponseEntity<UserLookupResponse> response = rt.exchange(
                uri,
                HttpMethod.GET,
                userResponseEntity,
                UserLookupResponse.class
        );

        User result = null;
        if (response.getBody() != null && response.getBody().data() != null) {
            result = userConverter.toUsers(response.getBody().data());
        } else {
            logger.warn("No user found with name {}", name);
        }
        return result;
    }
}
