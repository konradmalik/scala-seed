// run enable twice, workaround.
// Don't know why but suggested approach with semantic in ThisBuild does not work
// while scalafix seems to always fail after scalafixEnable is run second time in the same sbt session
addCommandAlias("scalafixAll", "scalafixEnable; all compile:scalafix test:scalafix; scalafmtAll; scalafixEnable")

shellPrompt := { s => Project.extract(s).currentProject.id + " > " }

import konradmalik.Dependencies._

inThisBuild(
  List(
    organization := "io.github.konradmalik",
    scalaVersion := scalaVersionNumber,
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.3.0"
  )
)

// If you Spark people want to include "provided" dependencies back to run,
// run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in(Compile, run), runner in(Compile, run)).evaluated

// modules
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
  typesafe ++ logback ++ scalaReflect ++ scalaTest

// SETTINGS
lazy val settings =
  commonSettings

lazy val compilerOptions = Seq(
  "-target:jvm-1.8",
  "-unchecked",
  "-feature",
  "-Ywarn-unused",
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
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case "application.conf" | "reference.conf" => MergeStrategy.concat
    case "logback.xml" => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)
