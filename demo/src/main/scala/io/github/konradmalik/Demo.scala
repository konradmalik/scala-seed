package io.github.konradmalik

import io.github.konradmalik.config.DemoConfig

object Demo extends DemoConfig {

  def main(args: Array[String]): Unit = {
    println(s"Hello from Scala! test config value is: $DEMO_TEST")
  }
}
