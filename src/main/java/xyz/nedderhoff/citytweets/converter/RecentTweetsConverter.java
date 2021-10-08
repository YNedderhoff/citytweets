package xyz.nedderhoff.citytweets.converter;

import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.cache.UserCache;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.domain.http.recentsearch.RecentSearchData;

import java.util.List;

@Component
public class RecentTweetsConverter {

    private final UserCache userCache;

    public RecentTweetsConverter(UserCache userCache) {
        this.userCache = userCache;
    }

    public List<Tweet> toTweets(List<RecentSearchData> data) {
        return data.stream()
                .map(d ->
                        new Tweet(
                                d.id(),
                                d.lang(),
                                d.text(),
                                userCache.getById(d.authorId())
                        )
                )
                .toList();
    }
}
