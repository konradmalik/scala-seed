import sbt._

object Dependencies {
  lazy val scalaBinaryVersionNumber = "2.12"
  lazy val scalaSubreleaseVersionNumber = "11"
  lazy val scalaVersionNumber = s"$scalaBinaryVersionNumber.$scalaSubreleaseVersionNumber"
  lazy val devMode = false

  lazy val sparkDep = {
    val version = "2.4.5"
    val compatCassandraConnector = "2.4.3"
    val libs =
      Seq(
        "org.apache.spark" %% "spark-core" % version,
        "org.apache.spark" %% "spark-sql" % version,
        "com.datastax.spark" %% "spark-cassandra-connector" % compatCassandraConnector
      )
    if (devMode)
      libs
    else
      libs.map(x => x % "provided")
  }

  // preferably same ver as spark cassandra uses
  lazy val cassandra = {
    val v = "3.7.2"
    Seq(
      "com.datastax.cassandra" % "cassandra-driver-core" % v,
      "com.datastax.cassandra" % "cassandra-driver-mapping" % v
    )
  }

  // use jackson compatible with spark version
  lazy val jacksonSparkCompatible = {
    val compatVer = "2.6.7"
    val version = compatVer + ".1"
    Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % version,
      "com.fasterxml.jackson.core" % "jackson-databind" % version,
      //      "com.fasterxml.jackson.core" % "jackson-core" % compatVer,
      // compatible jackson in order to print Instant as timestamp string
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % compatVer
    )
  }

  lazy val typesafe = Seq("com.typesafe" % "config" % "1.4.0")
  lazy val logback = Seq("ch.qos.logback" % "logback-classic" % "1.2.3")
  lazy val scalaReflect = Seq("org.scala-lang" % "scala-reflect" % scalaVersionNumber)
}
