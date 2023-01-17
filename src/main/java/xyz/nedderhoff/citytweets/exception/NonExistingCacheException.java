package xyz.nedderhoff.citytweets.exception;

public abstract class NonExistingCacheException extends RuntimeException {
    public NonExistingCacheException(String message) {
        super(message);
    }

    public NonExistingCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingCacheException(Throwable cause) {
        super(cause);
    }
}
