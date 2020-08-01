FROM openjdk:11.0.8-jre-slim

RUN apt-get update \
    && apt-get install curl=7.64.0-4+deb10u1 jq=1.5+dfsg-2+b1 tini=0.18.0-1 --assume-yes --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

COPY target/citytweets-*.jar /opt/citytweets/citytweets.jar
COPY entrypoint.sh /opt/citytweets

EXPOSE 8080

WORKDIR /opt/citytweets

ENTRYPOINT ["tini", "--", "/opt/citytweets/entrypoint.sh"]