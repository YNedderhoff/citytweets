package xyz.nedderhoff.citytweets.exception.twitter;

public class TwitterException extends RuntimeException {
    public TwitterException(String message) {
        super(message);
    }

    public TwitterException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwitterException(Throwable cause) {
        super(cause);
    }
}
