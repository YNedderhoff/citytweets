package xyz.nedderhoff.citytweets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "account-properties")
public record AccountProperties(
        TwitterProperties twitter,
        MastodonProperties mastodon
) {
    public interface PlatformProperties<T extends Account> {
        List<T> accounts();

        List<String> ignoredAccounts();
    }

    public record TwitterProperties(
            @Override
            List<TwitterAccount> accounts,
            List<String> ignoredAccounts
    ) implements PlatformProperties<TwitterAccount> {
    }

    public record MastodonProperties(
            @Override
            List<MastodonAccount> accounts,
            List<String> ignoredAccounts
    ) implements PlatformProperties<MastodonAccount> {
    }

    public record TwitterAccount(
            @Override
            String name,
            String bearerToken,
            Twitter4j twitter4j,
            String search,
            @Override
            String locationSearch,
            @Override
            String locationToFollow,
            @Override
            List<String> ignoredAccounts
    ) implements Account {
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

    public interface Account {
        String name();

        String locationSearch();

        String locationToFollow();

        List<String> ignoredAccounts();
    }


    public record MastodonAccount(
            @Override
            String name,
            String instance,
            Oauth oauth,
            String redirectUri,
            @Override
            String locationSearch,
            @Override
            String locationToFollow,
            @Override
            List<String> ignoredAccounts
    ) implements Account {

        public record Oauth(
                String accessToken,
                String accessTokenSecret
        ) {
        }

    }
}
