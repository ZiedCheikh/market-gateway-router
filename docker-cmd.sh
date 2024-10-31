#!/bin/sh
set -e
JAVA_OPTS="-Xms256m -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=DUMPS"

exec java $JAVA_OPTS \
  -jar -Dspring.profiles.active=dev /project/app.jar
