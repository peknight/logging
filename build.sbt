ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.2"

ThisBuild / organization := "com.peknight"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    "-Xmax-inlines:64"
  ),
)

lazy val logging = (project in file("."))
  .aggregate(
    loggingConfig,
  )
  .settings(commonSettings)
  .settings(
    name := "logging",
  )

lazy val loggingConfig = (project in file("logging-config"))
  .aggregate(
    logbackConfig.jvm,
    logbackConfig.js,
  )
  .settings(commonSettings)
  .settings(
    name := "logging-config",
  )

lazy val logbackConfig = (crossProject(JSPlatform, JVMPlatform) in file("logging-config/logback-config"))
  .settings(commonSettings)
  .settings(
    name := "logback-config",
    libraryDependencies ++= Seq(
    ),
  )
