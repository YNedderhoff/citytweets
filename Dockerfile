# Currently using openjdk as it supports linux/amd64 and linux/arm64/v8
# May switch back to temurin once it does as well

FROM openjdk:17-slim as builder

WORKDIR /application
ARG JAR_FILE=target/citytweets-*[0-9T].jar
COPY ${JAR_FILE} citytweets.jar
RUN java -Djarmode=layertools -jar citytweets.jar extract

FROM openjdk:17-slim

WORKDIR /application

RUN mkdir /dumps
RUN mkdir /opt/java
# create symlink from weird openjdk location to standard java path
RUN ln -sf /usr/local/openjdk-17 /opt/java/openjdk

COPY entrypoint.sh entrypoint.sh

# hadolint ignore=DL3018
RUN apt-get update \
    && apt-get install curl jq tini procps htop --assume-yes --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["tini", "--", "sh", "entrypoint.sh"]
