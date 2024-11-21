#!/bin/sh
set -e

ENVIRONMENT=${ENVIRONMENT:-dev}

echo "Starting app in environment: $ENVIRONMENT"

JAVA_OPTS="-Xms256m -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=DUMPS"

exec java $JAVA_OPTS \
  -jar -Dspring.profiles.active=$ENVIRONMENT /project/app.jar