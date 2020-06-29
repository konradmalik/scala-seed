package io.github.konradmalik.spark

import io.github.konradmalik.config.{CassandraConfig, SparkConfig}
import io.github.konradmalik.util.Using
import org.apache.spark.sql.SparkSession

/**
 * Provides implementing classes with Spark-related variables and methods
 */
trait SparkEnv {

  import SparkEnv._

  /**
   * Allows to run a given function using Spark
   *
   * @param f function to run
   * @tparam T Type returned from the function
   * @return 'f' return value
   */
  final def withSparkSession[T](f: SparkSession => T): T = Using.withResources(createSparkSession)(f)
}

/**
 * Companion object for [[SparkEnv]] containing static methods
 */
private object SparkEnv extends SparkConfig with CassandraConfig {

  /**
   * Creates a SparkSession with env-provided application name
   */
  final def createSparkSession: SparkSession = createSparkSession(SPARK_APP_NAME)

  /**
   * Creates a SparkSession with a given app name
   *
   * @param appName name of the Spark app
   * @return SparkSession
   */
  final def createSparkSession(appName: String): SparkSession = {
    val spark = SparkSession.builder()

    SPARK_MASTER.foreach(sm => spark.config("spark.master", sm))

    spark.config("spark.app.name", appName)
    // kryo
    spark.config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    // allow to run multiple RDDs in parallel when using async
    spark.config("spark.scheduler.mode", "FAIR")
    // cassandra
    spark.config("spark.cassandra.connection.host", CASSANDRA_HOST)
    spark.config("spark.cassandra.auth.username", CASSANDRA_USERNAME)
    spark.config("spark.cassandra.auth.password", CASSANDRA_PASSWORD)
    spark.getOrCreate()
  }
}
