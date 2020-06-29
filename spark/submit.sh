#!/bin/bash

echo "Submit main class ${MAIN_CLASS} to Spark master ${SPARK_MASTER_URL}"
echo "Additional Spark submit arguments ${SPARK_SUBMIT_ARGS}"
spark-submit \
  --class ${MAIN_CLASS} \
  --files docker.application.conf \
  --master ${SPARK_MASTER_URL} \
  --driver-java-options -Dconfig.file=docker.application.conf \
  ${SPARK_SUBMIT_ARGS} \
  app.jar
