package xyz.nedderhoff.citytweets.exception.mastodon;

public class MastodonException extends RuntimeException {
    public MastodonException(String message) {
        super(message);
    }

    public MastodonException(String message, Throwable cause) {
        super(message, cause);
    }

    public MastodonException(Throwable cause) {
        super(cause);
    }
}
