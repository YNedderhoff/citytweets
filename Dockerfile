# The official adoptopenjdk image (adoptopenjdk:11) is ubuntu-based which is tricky on arm/v7,
# so I am using an unoffical debian one instead that has at least arm/v7, arm64, and amd64:
#https://github.com/AdoptOpenJDK/openjdk-docker#official-and-unofficial-images
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-debian

RUN apt-get update \
    && apt-get install curl jq tini --assume-yes --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

COPY target/citytweets-*.jar /opt/citytweets/citytweets.jar
COPY entrypoint.sh /opt/citytweets

EXPOSE 8080

WORKDIR /opt/citytweets

ENTRYPOINT ["tini", "--", "/opt/citytweets/entrypoint.sh"]