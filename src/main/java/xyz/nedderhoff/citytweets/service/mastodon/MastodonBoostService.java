package xyz.nedderhoff.citytweets.service.mastodon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.nedderhoff.citytweets.api.mastodon.api1.AccountsEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api1.StatusEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api2.AuthEndpoint;
import xyz.nedderhoff.citytweets.api.mastodon.api2.SearchEndpoint;
import xyz.nedderhoff.citytweets.cache.mastodon.RetootCache;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;
import xyz.nedderhoff.citytweets.service.AbstractRepostService;

import java.util.Collections;
import java.util.function.Consumer;

@Service
public class MastodonBoostService extends AbstractRepostService<String, MastodonAccount, RetootCache, MastodonAccountService> {
    private static final Logger logger = LoggerFactory.getLogger(MastodonBoostService.class);
    private final AuthEndpoint authEndpoint;
    private final SearchEndpoint searchEndpoint;
    private final AccountsEndpoint accountsEndpoint;
    private final StatusEndpoint statusEndpoint;


    public MastodonBoostService(
            MastodonAccountService accountService,
            AuthEndpoint authEndpoint,
            SearchEndpoint searchEndpoint,
            AccountsEndpoint accountsEndpoint,
            StatusEndpoint statusEndpoint,
            RetootCache retootCache
    ) {
        super(retootCache, accountService);
        this.authEndpoint = authEndpoint;
        this.searchEndpoint = searchEndpoint;
        this.accountsEndpoint = accountsEndpoint;
        this.statusEndpoint = statusEndpoint;
    }

    @Override
    public void repost() {
        if (accountService.getAccounts() == null) {
            logger.info("No Mastodon accounts configured - skipping ...");
        } else {
            logger.warn("Mastodon accounts configured, but skipped in code!");
            boost();
        }
    }

    private void boost() {
        accountService.getAccounts().forEach(mastodonAccount -> {
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
                            .forEach(status -> cache(status.id(), mastodonAccount)));
        });
    }

    private boolean statusMentionsOwnAccount(Status status, MastodonAccount account) {
        final boolean mentionsOwnAccount = status.mentions().stream()
                .anyMatch(mention -> mention.username().equals(account.name()));
        if (mentionsOwnAccount) {
            logger.warn("Toot {} from user {} mentions own account:\n{}", status.id(), status.account().webfingerUri(), status.spoiler_text());
        }
        return mentionsOwnAccount;
    }

    private boolean shouldRetoot(Status status, MastodonAccount account) {
        final Consumer<String> hasBeenSeenLogger = (id) ->
                logger.warn("Toot {} from user {} was already reposted:\n{}", id, status.account().webfingerUri(), status.spoiler_text());
        final Consumer<String> authorBlockedLogger = (username) ->
                logger.warn("Toot {} from user {} can't be reposted as author is blocked:\n{}", status.id(), username, status.spoiler_text());

        return statusMentionsOwnAccount(status, account)
                && !isFromMe(status, account)
                && !hasBeenSeen(status.id(), account, hasBeenSeenLogger)
                && !isAuthorBlocked(status.account().webfingerUri(), account, authorBlockedLogger);
    }


    protected boolean isFromMe(Status status, MastodonAccount account) {
        final boolean isFromMe = status.account().webfingerUri().equals(account.name() + "@" + account.instance());
        if (isFromMe) {
            logger.warn("Toot {} from user {} is from me:\n{}", status.id(), status.account().webfingerUri(), status.spoiler_text());
        }
        return isFromMe;
    }
}
