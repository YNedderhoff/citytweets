# Currently using -focal as it supports linux/amd64, linux/arm/v7, and linux/arm64/v8
# May switch back to -alpine once it does as well
FROM eclipse-temurin:17-focal

# hadolint ignore=DL3018
RUN apt-get update \
    && apt-get install curl jq tini --assume-yes --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir /dumps

COPY entrypoint.sh /citytweets/entrypoint.sh
COPY target/citytweets-*[0-9T].jar /citytweets/citytweets.jar

WORKDIR /citytweets
ENTRYPOINT ["tini", "--", "sh", "/citytweets/entrypoint.sh"]
