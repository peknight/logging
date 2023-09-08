ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

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
    loggingCore.jvm,
    loggingCore.js,
    loggingConfig,
  )
  .settings(commonSettings)
  .settings(
    name := "logging",
  )

lazy val loggingCore = (crossProject(JSPlatform, JVMPlatform) in file("logging-core"))
  .settings(commonSettings)
  .settings(
    name := "logging-core",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion,
    ),
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
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-web" % "3.1.3"
    )
  )

val catsVersion = "2.10.0"
