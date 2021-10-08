FROM openjdk:17

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
