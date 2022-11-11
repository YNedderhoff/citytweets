package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.cache.FriendCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.Account;
import xyz.nedderhoff.citytweets.domain.Tweet;
import xyz.nedderhoff.citytweets.platform.twitter.api1.FollowEndpoint;
import xyz.nedderhoff.citytweets.platform.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.platform.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.service.AccountService;

@Component
@EnableScheduling
public class FollowJob {
    private static final Logger logger = LoggerFactory.getLogger(FollowJob.class);
    private static final int FOLLOW_RATE = 1000 * 60 * 60 * 24;

    private final MeEndpoint meEndpoint;
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final FollowEndpoint followEndpoint;
    private final FriendCache friendCache;
    private final AccountService accountService;

    @Autowired
    public FollowJob(
            MeEndpoint meEndpoint,
            RecentTweetsEndpoint recentTweetsEndpoint,
            FollowEndpoint followEndpoint,
            FriendCache friendCache,
            AccountService accountService
    ) {
        this.meEndpoint = meEndpoint;
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.followEndpoint = followEndpoint;
        this.friendCache = friendCache;
        this.accountService = accountService;
    }

    @Scheduled(fixedRate = FOLLOW_RATE)
    public void findPotentialFollowers() {
        accountService.getAccounts().forEach(account -> {
            logger.info("Looking for tweets for search {} in order to find followers for account {}", account.locationSearch(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.locationSearch()).stream()
                        .filter(tweet -> shouldFollow(tweet, myId, account))
                        .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                                tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                        )
                        .forEach(tweet -> followEndpoint.follow(tweet.user(), account));
            } catch (TwitterException e) {
                logger.error("Exception during follow job", e);
            }
        });
    }

    private boolean shouldFollow(Tweet tweet, long myId, Account account) {
        return !isTweetFromMe(tweet, myId)
                && isMaybeFromDesiredLocation(tweet, account)
                && !hasBeenSeen(tweet, account);

    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isMaybeFromDesiredLocation(Tweet tweet, Account account) {
        return tweet.user().location().toLowerCase().contains(account.locationToFollow().toLowerCase());
    }

    private boolean hasBeenSeen(Tweet tweet, Account account) {
        return friendCache.contains(tweet.user().id(), account);
    }
}
