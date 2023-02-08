# Currently using openjdk as it supports linux/amd64 and linux/arm64/v8
# May switch back to temurin once it does as well

FROM openjdk:17-slim as builder

ARG JAR_FILE=target/citytweets-*[0-9T].jar
COPY ${JAR_FILE} /citytweets/citytweets.jar
RUN java -Djarmode=layertools -jar /citytweets/citytweets.jar extract

FROM openjdk:17-slim

# hadolint ignore=DL3018
RUN apt-get update \
    && apt-get install curl jq tini procps htop --assume-yes --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir /dumps

# create symlink from weird openjdk location to standard java path
RUN mkdir /opt/java
RUN ln -sf /usr/local/openjdk-17 /opt/java/openjdk

COPY entrypoint.sh /citytweets/entrypoint.sh
COPY --from=builder dependencies/ ./citytweets/
COPY --from=builder snapshot-dependencies/ ./citytweets/
COPY --from=builder spring-boot-loader/ ./citytweets/
COPY --from=builder application/ ./citytweets/

WORKDIR /citytweets
ENTRYPOINT ["tini", "--", "sh", "/citytweets/entrypoint.sh"]
