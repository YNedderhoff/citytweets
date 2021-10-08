# Using focal over eclipse-temurin:17 as it works for armv7 and arm64 as well.
FROM eclipse-temurin:17-focal

RUN apt-get update \
    && apt-get install curl jq tini --assume-yes --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

COPY target/citytweets-*.jar /opt/citytweets/citytweets.jar
COPY entrypoint.sh /opt/citytweets

EXPOSE 8080

WORKDIR /opt/citytweets

ENTRYPOINT ["tini", "--", "/opt/citytweets/entrypoint.sh"]
