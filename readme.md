# Scala Spark Cassandra SBT multi-module seed

A simple seed project to use for Spark and Scala programming and docker-based deployment.

Uses muli-staged Docker building procedure, see Dockerfiles and Makefile for details.

Contains Spark and corresponding Cassandra connector. Those can be easily deleted if you 
don't need them and just want to use plain Scala.

If you dont need to use modules, those can also be easily converted into just 1 module in build.sbt.

Dependencies are abstracted-out to `project/Dependencies.scala` file.

Uses sbt-assembly to produce fat-jars.

Configuration based on Typesafe Config files. Separate overrides file for docker (production) and possibility to use ENV variables.

Focuses on simplicity and modularity.

The focal point is Makefile, see it for available commands.
