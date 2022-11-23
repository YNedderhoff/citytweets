package xyz.nedderhoff.citytweets.domain.mastodon.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Search(
        List<Account> accounts
) {
}
