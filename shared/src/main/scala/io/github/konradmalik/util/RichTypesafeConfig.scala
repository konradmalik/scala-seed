package io.github.konradmalik.util

import com.typesafe.config.Config

/**
  * Container for classes enriching standard Typesafe config classes
  */
object RichTypesafeConfig {

  /**
    * Creates a richer config class
    *
    * @param underlying base config instance
    */
  implicit class RichConfig(val underlying: Config) extends AnyVal {

    /**
      * Gets string as an [[Option]]
      *
      * @param path path to get from
      * @return Optional string
      */
    def getOptionalString(path: String): Option[String] =
      if (underlying.hasPath(path)) {
        Some(underlying.getString(path))
      } else {
        None
      }
  }

}
