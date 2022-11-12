package xyz.nedderhoff.citytweets.config;

import java.util.List;

public abstract class Account<T extends AccountApiConfig> {
    private final String name;
    private final String search;
    private final String locationSearch;
    private final String locationToFollow;
    private final List<String> ignoredAccounts;

    private final T accountApiConfig;

    public Account(
            String name,
            String search,
            String locationSearch,
            String locationToFollow,
            List<String> ignoredAccounts,
            T accountApiConfig
    ) {
        this.name = name;
        this.search = search;
        this.locationSearch = locationSearch;
        this.locationToFollow = locationToFollow;
        this.ignoredAccounts = ignoredAccounts;
        this.accountApiConfig = accountApiConfig;
    }

    public String getName() {
        return name;
    }

    public String getSearch() {
        return search;
    }

    public String getLocationSearch() {
        return locationSearch;
    }

    public String getLocationToFollow() {
        return locationToFollow;
    }

    public List<String> getIgnoredAccounts() {
        return ignoredAccounts;
    }

    public T getAccountApiConfig() {
        return accountApiConfig;
    }
}
