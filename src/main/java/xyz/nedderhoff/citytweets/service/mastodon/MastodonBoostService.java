package xyz.nedderhoff.citytweets.service.mastodon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.api.mastodon.api1.AccountsEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api1.StatusEndpoint;
import xyz.nedderhoff.citytweets.cache.mastodon.RetootCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;
import xyz.nedderhoff.citytweets.service.AbstractRepostService;

import java.util.function.Consumer;

@Service
public class MastodonBoostService extends AbstractRepostService<String, MastodonAccount, RetootCache, MastodonAccountService> {
    private static final Logger logger = LoggerFactory.getLogger(MastodonBoostService.class);
    private final AccountsEndpoint accountsEndpoint;
    private final StatusEndpoint statusEndpoint;


    public MastodonBoostService(
            MastodonAccountService accountService,
            AccountsEndpoint accountsEndpoint,
            StatusEndpoint statusEndpoint,
            RetootCache retootCache
    ) {
        super(retootCache, accountService);
        this.accountsEndpoint = accountsEndpoint;
        this.statusEndpoint = statusEndpoint;
    }

    @Override
    public void repost() {
        logger.info("Running Mastodon repost job in thread {}", Thread.currentThread().getName());
        if (accountService.getAccounts() == null) {
            logger.info("No Mastodon accounts configured - skipping ...");
        } else {
            boost();
        }
    }

    private void boost() {
        accountService.getAccounts().forEach(mastodonAccount -> {
            logger.info("Looking for unseen toots mentioning Mastodon account {}", mastodonAccount.name());
            accountsEndpoint.getFollowers(mastodonAccount)
                    .stream()
                    .flatMap(follower -> accountsEndpoint.getStatuses(follower, mastodonAccount).stream())
                    .filter(status -> shouldRetoot(status, mastodonAccount))
                    .map(status -> statusEndpoint.boost(status, mastodonAccount))
                    .forEach(status -> cache(status.reblog().id(), mastodonAccount));
        });
    }

    private boolean statusMentionsOwnAccount(Status status, MastodonAccount account) {
        final boolean mentionsOwnAccount = status.mentions().stream()
                .anyMatch(mention -> mention.username().equals(account.name()));
        if (mentionsOwnAccount) {
            logger.warn(
                    "Toot {} from user {} mentions own account:{}",
                    status.id(), status.account().webfingerUri(), status.url()
            );
        }
        return mentionsOwnAccount;
    }

    private boolean shouldRetoot(Status status, MastodonAccount account) {
        final Consumer<String> hasBeenSeenLogger = (id) ->
                logger.warn("Toot {} from user {} was already reposted: {}", id, status.account().webfingerUri(), status.url());
        final Consumer<String> authorBlockedLogger = (username) ->
                logger.warn("Toot {} from user {} can't be reposted as author is blocked: {}", status.id(), username, status.url());

        return !hasRebloggedStatus(status)
                && !hasBeenSeen(status.id(), account, hasBeenSeenLogger)
                && statusMentionsOwnAccount(status, account)
                && !isFromMe(status, account)
                && !isAuthorBlocked(status.account().webfingerUri(), account, authorBlockedLogger);
    }

    private static boolean hasRebloggedStatus(Status status) {
        final boolean reblogged = status.reblogged();

        if (reblogged) {
            logger.warn(
                    "Toot {} from user {} has been reposted already according to Mastodon: {}",
                    status.id(), status.account().webfingerUri(), status.url()
            );
        }
        return reblogged;
    }


    protected boolean isFromMe(Status status, MastodonAccount account) {
        final boolean isFromMe = status.account().webfingerUri().equals(account.name() + "@" + account.instance());
        if (isFromMe) {
            logger.warn("Toot {} from user {} is from me: {}", status.id(), status.account().webfingerUri(), status.url());
        }
        return isFromMe;
    }
}
