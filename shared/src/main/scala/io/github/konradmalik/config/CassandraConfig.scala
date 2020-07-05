package io.github.konradmalik.config

import scala.concurrent.duration.Duration

/**
  * Provides implementing classes with Cassandra configurations
  */
trait CassandraConfig {

  import CassandraConfig._

  /**
    * Hostname
    */
  lazy final val CASSANDRA_HOST = config.getString("host")

  /**
    * Port
    */
  lazy final val CASSANDRA_PORT = config.getInt("port")

  /**
    * Username
    */
  lazy final val CASSANDRA_USERNAME = config.getString("username")

  /**
    * Plaintext password
    */
  lazy final val CASSANDRA_PASSWORD = config.getString("password")

  /**
    * Replication used when creating cassandra keyspaces programatically
    */
  lazy final val CASSANDRA_REPLICATION = config.getInt("replication")

  /**
    * List of keyspaces to not include when listing keyspaces
    */
  lazy final val KEYSPACES_TO_OMIT = Set("system")

  /**
    * Timeout of the executed queries
    */
  lazy final val QUERY_TIMEOUT =
    Duration.fromNanos(config.getDuration("queryTimeout").toNanos)
}

/**
  * Private companion to create config instance
  */
private object CassandraConfig {
  private lazy final val config =
    TypesafeConfig.coreConfig.getConfig("cassandra")
}
