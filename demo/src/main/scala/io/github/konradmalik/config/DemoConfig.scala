package io.github.konradmalik.config

trait DemoConfig {

  import DemoConfig._

  lazy val DEMO_TEST: String = demoConfig.getString("test")
}

/**
  * Private companion to create config instances
  */
private object DemoConfig {
  lazy final val demoConfig = TypesafeConfig.coreConfig.getConfig("demo")
}
