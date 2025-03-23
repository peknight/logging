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
    loggingCore.jvm,
    loggingCore.js,
    loggingNatchez.jvm,
    loggingNatchez.js,
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
      "com.peknight" %%% "log4cats-ext" % pekExtVersion,
      "com.peknight" %%% "error-core" % pekErrorVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
    ),
  )

lazy val loggingNatchez = (crossProject(JSPlatform, JVMPlatform) in file("logging-natchez"))
  .dependsOn(loggingCore)
  .settings(commonSettings)
  .settings(
    name := "logging-natchez",
    libraryDependencies ++= Seq(
      "org.tpolecat" %%% "natchez-core" % natchezVersion,
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

val catsEffectVersion = "3.5.7"
val log4CatsVersion = "2.7.0"
val natchezVersion = "0.3.7"
val pekVersion = "0.1.0-SNAPSHOT"
val pekExtVersion = pekVersion
val pekErrorVersion = pekVersion
val log4CatsCore = "org.typelevel" %% "log4cats-core" % log4CatsVersion
