#!/bin/bash

exec /opt/java/openjdk/bin/java -Dinst.type=citytweets \
    -Dcitytweets.inst.id="$HOSTNAME" \
    "${JVM_PARAMETERS:-}" \
    "${JAVA_OPTS:-}" \
    "${JAVA_OPTS_GC:-}" \
    "${JAVA_OPTS_MEM:-}" \
    "${JAVA_OPTS_DEBUG:-}" \
    -XX:OnOutOfMemoryError='kill -9 %p' \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/dumps/citytweets.bin \
    -jar citytweets.jar \
    $@