 # citytweets
 
![Build, Test and Publish](https://github.com/ynedderhoff/citytweets/workflows/Build,%20Test%20and%20Publish/badge.svg) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/d8280dad48c6491caa91e7241c48ccb1)](https://app.codacy.com/manual/YNedderhoff/citytweets?utm_source=github.com&utm_medium=referral&utm_content=YNedderhoff/citytweets&utm_campaign=Badge_Grade_Dashboard)

## About
citytweets is a Twitter retweet bot currently powering my accounts [@luebbecketweets](https://twitter.com/luebbecketweets), [@mindentweets](https://twitter.com/mindentweets) and [@herfordtweets](https://twitter.com/herfordtweets). In addition to my accounts, it also powers [@TweetsFuerMiLB](https://twitter.com/TweetsFuerMiLB), which is not owned by me.

It retweets everything the search finds. It is intended to be used with searches like `@mindentweets` so that everyone can at-mention the account `@mindentweets` and will get a retweet, thus reach every follower of `@mindentweets`.

The bot's own Twitter account can be found at [@citytweets_bot](https://twitter.com/citytweets_bot).

## Tech
For some calls it still relies on the deprecated [Twitter API 1.1](https://developer.twitter.com/en/docs/twitter-api/v1). For calls to this API it uses [Twitter4J](https://github.com/Twitter4J/Twitter4J), which does not seem to be maintained anymore as well.
For other calls, and especially the calls that actually search for new tweets, it already relies on the new [Twitter API 2.0](https://developer.twitter.com/en/docs/twitter-api/early-access) to which I got early access. These calls are made without any library help. Further migration will happen over time. 
