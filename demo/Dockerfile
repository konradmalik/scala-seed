ARG owner
ARG project
FROM ${owner}/${project}-builder AS builder
FROM openjdk:8-alpine
ARG module_name
ARG scala_binary_ver

MAINTAINER Konrad Malik <konrad.malik@gmail.com>

RUN apk add --no-cache bash
WORKDIR /app
COPY --from=builder /build/${module_name}/target/scala-${scala_binary_ver}/${module_name}.jar app.jar

COPY docker.application.conf docker.application.conf
ENV CASSANDRA_HOST=cassandra
ENV CASSANDRA_PORT=9042
ENV CASSANDRA_USERNAME=cassandra
ENV CASSANDRA_PASSWORD=cassandra
ENV TEST=test

CMD ["java", "-Dconfig.file=docker.application.conf", "-jar", "app.jar"]
