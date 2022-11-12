package xyz.nedderhoff.citytweets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "twitter-account-properties")
public class TwitterAccountProperties {

    private final List<TwitterAccount> accounts;

    public TwitterAccountProperties(List<TwitterAccount> accounts) {
        this.accounts = accounts;
    }

    public List<TwitterAccount> getAccounts() {
        return accounts;
    }

}
