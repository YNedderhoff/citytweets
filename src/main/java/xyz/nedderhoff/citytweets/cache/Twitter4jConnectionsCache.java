package xyz.nedderhoff.citytweets.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import xyz.nedderhoff.citytweets.config.TwitterAccount;
import xyz.nedderhoff.citytweets.config.TwitterAccountApiConfig.Twitter4j.Oauth;
import xyz.nedderhoff.citytweets.service.AccountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class Twitter4jConnectionsCache {
    private static final Logger logger = LoggerFactory.getLogger(Twitter4jConnectionsCache.class);

    private final Map<String, Twitter> connections = new HashMap<>();

    @Autowired
    public Twitter4jConnectionsCache(AccountService accountService) {
        accountService.getAccounts()
                .forEach(account -> connections.put(account.getName(), createTwitter4jConnection(account)));
    }

    public Twitter getConnection(TwitterAccount account) {
        return connections.get(account.getName());
    }

    public Twitter getRandomConnection() {
        List<String> keyList = new ArrayList<>(connections.keySet());

        int size = keyList.size();
        int randIdx = new Random().nextInt(size);

        String randomKey = keyList.get(randIdx);

        return connections.get(randomKey);
    }

    private static Twitter createTwitter4jConnection(TwitterAccount account) {
        logger.info("Creating Twitter4J connection for account {}", account.getName());
        final Oauth oauth = account.getAccountApiConfig().getTwitter4j().oauth();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(oauth.consumerKey())
                .setOAuthConsumerSecret(oauth.consumerSecret())
                .setOAuthAccessToken(oauth.accessToken())
                .setOAuthAccessTokenSecret(oauth.accessTokenSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
