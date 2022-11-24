package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import xyz.nedderhoff.citytweets.api.mastodon.api1.AccountsEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api1.StatusEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api2.AuthEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api2.SearchEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.MeEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api1.RetweetEndpoint;
import xyz.nedderhoff.citytweets.api.twitter.api2.RecentTweetsEndpoint;
import xyz.nedderhoff.citytweets.cache.mastodon.RetootCache;
import xyz.nedderhoff.citytweets.cache.twitter.RetweetCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;
import xyz.nedderhoff.citytweets.domain.twitter.Tweet;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.Collections;

@Component
@EnableScheduling
public class RetweetJob {
    private static final Logger logger = LoggerFactory.getLogger(RetweetJob.class);
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final AccountService accountService;

    // Twitter Dependencies
    private final RecentTweetsEndpoint recentTweetsEndpoint;
    private final RetweetEndpoint retweetEndpoint;
    private final MeEndpoint meEndpoint;
    private final RetweetCache retweetCache;

    // Mastodon Dependencies
    private final AuthEndpoint authEndpoint;
    private final SearchEndpoint searchEndpoint;
    private final AccountsEndpoint accountsEndpoint;
    private final StatusEndpoint statusEndpoint;
    private final RetootCache retootCache;


    @Autowired
    public RetweetJob(
            AccountService accountService,
            RecentTweetsEndpoint recentTweetsEndpoint,
            RetweetEndpoint retweetEndpoint,
            MeEndpoint meEndpoint,
            RetweetCache retweetCache,
            AuthEndpoint authEndpoint,
            SearchEndpoint searchEndpoint,
            AccountsEndpoint accountsEndpoint,
            StatusEndpoint statusEndpoint,
            RetootCache retootCache
    ) {
        this.recentTweetsEndpoint = recentTweetsEndpoint;
        this.retweetEndpoint = retweetEndpoint;
        this.meEndpoint = meEndpoint;
        this.retweetCache = retweetCache;
        this.accountService = accountService;
        this.authEndpoint = authEndpoint;
        this.searchEndpoint = searchEndpoint;
        this.accountsEndpoint = accountsEndpoint;
        this.statusEndpoint = statusEndpoint;
        this.retootCache = retootCache;
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void run() {
        runTwitter();
        runMastodon();
    }

    public void runTwitter() {
        accountService.getTwitterAccounts().forEach(account -> {
            logger.info("Looking for unseen tweets for search {} on Twitter account {}", account.search(), account.name());
            final long myId;
            try {
                myId = meEndpoint.getId(account);
                recentTweetsEndpoint.search(account.search()).stream()
                        .filter(tweet -> shouldRetweet(tweet, myId))
                        .peek(tweet -> logger.info("Found Tweet: ID \"{}\", Author \"{}\", Language \"{}\", Location \"{}\", Text \"{}\".",
                                tweet.id(), tweet.user().name(), tweet.lang(), tweet.user().location(), tweet.text())
                        )
                        .forEach(t -> retweetEndpoint.retweet(t, account));
            } catch (TwitterException e) {
                logger.error("Exception during retweet job", e);
            }
        });
    }

    private boolean shouldRetweet(Tweet tweet, long myId) {
        return !isTweetFromMe(tweet, myId)
                && !isRetweet(tweet)
                && !hasBeenSeen(tweet);
    }

    private boolean isTweetFromMe(Tweet tweet, long myId) {
        return tweet.user().id() == myId;
    }

    private boolean isRetweet(Tweet tweet) {
        return tweet.text().startsWith("RT @");
    }

    private boolean hasBeenSeen(Tweet tweet) {
        return retweetCache.contains(tweet.id());
    }

    public void runMastodon() {
        accountService.getMastodonAccounts().forEach(mastodonAccount -> {
            logger.info("Looking for unseen toots mentioning Mastodon account {}", mastodonAccount.name());
            authEndpoint.getHttpHeadersWithAuth(mastodonAccount)
                    .ifPresent(authedHeaders -> searchEndpoint.searchAccountId(authedHeaders, mastodonAccount)
                            .map(mastodonAccountId -> accountsEndpoint.getFollowers(mastodonAccountId, authedHeaders, mastodonAccount))
                            .orElseGet(() -> {
                                        logger.warn("Did not successfully fetch followers of {}", mastodonAccount.name());
                                        return Collections.emptyList();
                                    }
                            )
                            .stream()
                            .flatMap(follower -> accountsEndpoint.getStatuses(follower, authedHeaders, mastodonAccount).stream())
                            .filter(status -> shouldRetoot(status, mastodonAccount))
                            .map(status -> statusEndpoint.boost(status, authedHeaders, mastodonAccount))
                            .forEach(status ->  retootCache.add(status.id())));
        });
    }

    private boolean statusMentionsOwnAccount(Status status, MastodonAccount account) {
        return status.mentions().stream()
                .anyMatch(mention -> mention.username().equals(account.name()));
    }

    private boolean shouldRetoot(Status status, MastodonAccount account) {
        return statusMentionsOwnAccount(status, account)
                && !isTootFromMe(status, account)
                && !hasBeenSeen(status);
    }

    private boolean isTootFromMe(Status status, MastodonAccount account) {
        return status.account().webfingerUri().equals(account.name() + "@" + account.instance());
    }

    private boolean hasBeenSeen(Status status) {
        return retootCache.contains(status.id());
    }


}
