package xyz.nedderhoff.citytweets.config;

public class TwitterAccountApiConfig implements AccountApiConfig {

    private final String bearerToken;
    private final Twitter4j twitter4j;

    public TwitterAccountApiConfig(
            String bearerToken,
            Twitter4j twitter4j
    ) {
        this.bearerToken = bearerToken;
        this.twitter4j = twitter4j;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public Twitter4j getTwitter4j() {
        return twitter4j;
    }

    public record Twitter4j(
            Twitter4j.Oauth oauth
    ) {
        public record Oauth(
                String accessToken,
                String accessTokenSecret,
                String consumerKey,
                String consumerSecret
        ) {
        }
    }
}
