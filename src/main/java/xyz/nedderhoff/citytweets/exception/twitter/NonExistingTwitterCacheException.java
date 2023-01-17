package xyz.nedderhoff.citytweets.exception.twitter;

import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;

public class NonExistingTwitterCacheException extends NonExistingCacheException {
    public NonExistingTwitterCacheException(String message) {
        super(message);
    }

    public NonExistingTwitterCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingTwitterCacheException(Throwable cause) {
        super(cause);
    }
}
