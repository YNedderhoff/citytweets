package xyz.nedderhoff.citytweets.api.mastodon.api1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.nedderhoff.citytweets.api.mastodon.MastodonApi1Endpoint;
import xyz.nedderhoff.citytweets.config.AccountProperties.MastodonAccount;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Account;
import xyz.nedderhoff.citytweets.domain.mastodon.http.Status;

@Component
public class BoostEndpoint extends MastodonApi1Endpoint<Account[]> {
    private static final Logger logger = LoggerFactory.getLogger(BoostEndpoint.class);

    public BoostEndpoint(RestTemplate rt) {
        super(rt);
    }

    public void boost(Status status, HttpHeaders authedHeaders, MastodonAccount mastodonAccount) {
        logger.info("boosting status {} for {}", status.uri(), mastodonAccount.name());
    }
}
