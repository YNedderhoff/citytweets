package xyz.nedderhoff.citytweets.domain;

public class Tweet {
    private final long id;
    private final String lang;
    private final String text;
    private final User user;

    public Tweet(long id, String lang, String text, User user) {
        this.id = id;
        this.lang = lang;
        this.text = text;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getLang() {
        return lang;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", lang='" + lang + '\'' +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}
