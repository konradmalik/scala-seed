ARG owner
ARG project
FROM ${owner}/${project}-builder AS builder
FROM konradmalik/spark:latest
ARG module_name
ARG scala_binary_ver

MAINTAINER Konrad Malik <konrad.malik@gmail.com>

WORKDIR /app
COPY --from=builder /build/${module_name}/target/scala-${scala_binary_ver}/${module_name}.jar app.jar

COPY docker.application.conf docker.application.conf
COPY submit.sh submit.sh

ENV CASSANDRA_HOST=cassandra
ENV CASSANDRA_PORT=9042
ENV CASSANDRA_USERNAME=cassandra
ENV CASSANDRA_PASSWORD=cassandra
ENV CASSANDRA_REPLICATION=1
ENV SPARK_APP_NAME=SparkDemo
ENV MAIN_CLASS="io.github.konradmalik.spark.SparkDemo"
# spark submit
ENV SPARK_MASTER_URL="local[*]"

CMD ["/bin/bash", "submit.sh"]
