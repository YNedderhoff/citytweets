package xyz.nedderhoff.citytweets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "account-properties")
public record AccountProperties(
        List<Account> accounts
) {
    public record Account(
            String name,
            String bearerToken,
            Twitter4j twitter4j,
            String search,
            String locationSearch,
            String locationToFollow,
            List<String> ignoredAccounts
    ) {
        public record Twitter4j(
                Oauth oauth
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
}
