package xyz.nedderhoff.citytweets.config;

public enum Service {
    TWITTER("twitter"),
    MASTODON("mastodon");

    private final String name;

    Service(String type) {
        this.name = type;
    }

    public String getName() {
        return name;
    }
}
