package io.github.konradmalik.config

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Represents a typesafe config provided to the application.
 * Each 'reference.conf' is loaded first (those should be provided only by libraries,
 * not modules which have main methods). Then those values are overridden by 'application.conf'
 * (those should be provided by modules able to execute - with main methods).
 * Lastly, each docker.application.conf is copied to the images and it overrides all previous configs
 * with values that are provided as env variables.
 */
private[config] object TypesafeConfig {
  // extract configs
  /**
   * Core config section (root)
   */
  lazy val coreConfig: Config = userConfig
  /**
   * Reference config can be provided
   */
  private lazy val referenceConfig: Config =
    ConfigFactory.parseResources("reference.conf")
  /**
   * Application config can be provided
   */
  private lazy val applicationConfig: Config =
    ConfigFactory.parseResources("application.conf").withFallback(referenceConfig)
  /**
   * This tries to load mentioned 'reference' and 'application' confs.
   */
  private lazy val userConfig: Config = ConfigFactory.load.withFallback(applicationConfig)
}
