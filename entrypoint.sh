#!/bin/bash

# Ignoring shellcheck SC2086 errors. It would otherwise complain about missing quotes:
#
# exec java ${JVM_PARAMETERS:-} \
#           ^-----------------^ SC2086: Double quote to prevent globbing and word splitting.
#
# However we actually want word splitting, so that java arguments such as -Xone -Xtwo -Xthree are actually passed as
# separate arguments, an not one string, i.e. "-Xone -Xtwo -Xthree".
#
# shellcheck disable=SC2086
#

exec /opt/java/openjdk/bin/java -Dinst.type=citytweets \
    -Dcitytweets.inst.id=$HOSTNAME \
    ${JVM_PARAMETERS:-} \
    ${JAVA_OPTS:-} \
    ${JAVA_OPTS_GC:-} \
    ${JAVA_OPTS_MEM:-} \
    ${JAVA_OPTS_DEBUG:-} \
    -XX:OnOutOfMemoryError='kill -9 %p' \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/dumps/citytweets.bin \
    -jar citytweets.jar \
    $@
