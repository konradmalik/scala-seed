package io.github.konradmalik

import io.github.konradmalik.cassandra.CassandraEnv
import io.github.konradmalik.spark.SparkEnv
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object SparkDemo extends SparkEnv with CassandraEnv {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    logger.info("arguments: " + args.mkString("; "))

    withSparkSession { implicit spark =>
      val twoPlusTwo = spark.sparkContext
        .parallelize(Seq(2, 2))
        .reduce(_ + _)
      println(s"Hello from Spark! 2 + 2 is: $twoPlusTwo")
      withCassandraSession { implicit session =>
        println(s"Hello from Cassandra! Keyspaces: $getKeyspaces")
      }
    }
  }
}
