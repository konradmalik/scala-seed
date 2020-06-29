import Dependencies._

organization in ThisBuild := "io.github.konradmalik"
scalaVersion in ThisBuild := scalaVersionNumber
lazy val projectName = "scala-seed"
name := projectName

// If you Spark people want to include "provided" dependencies back to run,
// run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in(Compile, run), runner in(Compile, run)).evaluated

// modules
lazy val global = Project(projectName, file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    shared,
    spark
  )

lazy val shared = project
  .settings(
    name := "shared",
    settings,
    libraryDependencies ++= commonDependencies ++ jacksonSparkCompatible ++ cassandra
  )
  .disablePlugins(AssemblyPlugin)

lazy val demo = project
  .settings(
    name := "demo",
    settings,
    assemblySettings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    shared
  )

lazy val spark = project
  .settings(
    name := "spark",
    settings,
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ sparkDep,
    dependencyOverrides ++= jacksonSparkCompatible
  )
  .dependsOn(
    shared
  )

// DEPENDENCIES
lazy val commonDependencies =
  typesafe ++ logback ++ scalaReflect

// SETTINGS
lazy val settings =
  commonSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case "application.conf" | "reference.conf" => MergeStrategy.concat
    case "logback.xml" => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)
