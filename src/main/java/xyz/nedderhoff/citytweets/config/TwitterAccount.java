package xyz.nedderhoff.citytweets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "twitter-account-properties.accounts")
public class TwitterAccount extends Account<TwitterAccountApiConfig> {

    public TwitterAccount(
            String name,
            String search,
            String locationSearch,
            String locationToFollow,
            List<String> ignoredAccounts,
            TwitterAccountApiConfig accountApiConfig
    ) {
        super(name, search, locationSearch, locationToFollow, ignoredAccounts, accountApiConfig);
    }
}
