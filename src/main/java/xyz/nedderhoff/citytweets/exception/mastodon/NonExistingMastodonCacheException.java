package xyz.nedderhoff.citytweets.exception.mastodon;

import xyz.nedderhoff.citytweets.exception.NonExistingCacheException;

public class NonExistingMastodonCacheException extends NonExistingCacheException {
    public NonExistingMastodonCacheException(String message) {
        super(message);
    }

    public NonExistingMastodonCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingMastodonCacheException(Throwable cause) {
        super(cause);
    }
}
