package xyz.nedderhoff.citytweets.config;

public enum Service {
    TWITTER("twitter"),
    MASTODON("mastodon");

    private final String type;

    Service(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
