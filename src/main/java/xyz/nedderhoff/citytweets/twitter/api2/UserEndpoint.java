package xyz.nedderhoff.citytweets.twitter.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.converter.UserConverter;
import xyz.nedderhoff.citytweets.domain.User;
import xyz.nedderhoff.citytweets.domain.http.userlookup.UserLookupResponse;
import xyz.nedderhoff.citytweets.twitter.TwitterApi2Endpoint;

@Component
public class UserEndpoint extends TwitterApi2Endpoint {
    private static final Logger logger = LoggerFactory.getLogger(UserEndpoint.class);
    public static final String BASE_QUERY_USER_BY_ID = BASE_TWITTER_API_2_URI + "users/%d?user.fields=location";
    public static final String BASE_QUERY_USER_BY_NAME = BASE_TWITTER_API_2_URI + "users/by/username/%s?user.fields=location";

    private final Twitter twitter;
    private final UserConverter userConverter;
    private final HttpEntity<UserLookupResponse> userResponseEntity;

    @Autowired
    public UserEndpoint(
            Twitter twitter,
            RestTemplate rt,
            @Value("${bearerToken}") String bearerToken,
            UserConverter userConverter
    ) {
        super(rt, bearerToken);
        this.twitter = twitter;
        this.userConverter = userConverter;

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        userResponseEntity = new HttpEntity<>(headers);
    }

    public long getId() throws TwitterException {
        return twitter.getId();
    }

    public User getById(long id) {
        logger.info("Requesting user by id {}", id);

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
        logger.info("Requesting user by name {}", name);

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
