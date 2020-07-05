package io.github.konradmalik.config

/**
  * Provides implementing classes with Spark related configuration
  */
trait SparkConfig {

  import SparkConfig._
  import io.github.konradmalik.util.RichTypesafeConfig._

  /**
    * Name to assign for the Spark App
    */
  lazy final val SPARK_APP_NAME: String = config.getString("name")

  /**
    * Spark master full URL (with protocol and port)
    */
  lazy final val SPARK_MASTER: Option[String] =
    config.getOptionalString("master")
}

/**
  * Private companion to create config instance
  */
private object SparkConfig {
  lazy final val config = TypesafeConfig.coreConfig.getConfig("spark")
}
