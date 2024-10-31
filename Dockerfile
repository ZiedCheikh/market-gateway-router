FROM eclipse-temurin:21

ARG JAVA_PORT=8080

ADD docker-cmd.sh /project/

RUN apt-get update  && apt-get install -y curl tar \
   && rm -rf /var/lib/apt/lists/*

WORKDIR /project

RUN pwd

COPY build/libs/market-gateway-router-0.0.1-SNAPSHOT.jar /project/app.jar


EXPOSE ${JAVA_PORT}

RUN ["chmod", "777", "/project/app.jar"]
RUN ["chmod", "777", "/project/docker-cmd.sh"]

ENTRYPOINT ["/project/docker-cmd.sh"]
