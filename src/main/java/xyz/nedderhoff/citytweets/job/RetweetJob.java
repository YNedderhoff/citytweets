package xyz.nedderhoff.citytweets.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nedderhoff.citytweets.service.AccountService;
import xyz.nedderhoff.citytweets.service.impl.MastodonBoostService;
import xyz.nedderhoff.citytweets.service.impl.TwitterRetweetService;

@Component
@EnableScheduling
public class RetweetJob {
    private static final Logger logger = LoggerFactory.getLogger(RetweetJob.class);
    private static final int FETCHING_RATE = 1000 * 60 * 5;

    private final AccountService accountService;
    private final TwitterRetweetService twitterRetweetService;
    private final MastodonBoostService mastodonBoostService;

    public RetweetJob(
            AccountService accountService,
            TwitterRetweetService twitterRetweetService,
            MastodonBoostService mastodonBoostService
    ) {
        this.accountService = accountService;
        this.twitterRetweetService = twitterRetweetService;
        this.mastodonBoostService = mastodonBoostService;
    }

    @Scheduled(fixedRate = FETCHING_RATE)
    public void run() {
        if (accountService.getTwitterAccounts() == null) {
            logger.info("No Twitter accounts configured - skipping ...");
        } else {
            twitterRetweetService.run();
        }
        if (accountService.getMastodonAccounts() == null) {
            logger.info("No Mastodon accounts configured - skipping ...");
        } else {
            //TODO enable Mastodon if boost API ever becomes accessible without user auth
            logger.warn("Mastodon accounts configured, but skipped in code!");
            //mastodonBoostService.run();
        }
    }
}
