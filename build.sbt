import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val logging = (project in file("."))
  .aggregate(
    loggingCore.jvm,
    loggingCore.js,
    loggingNatchez.jvm,
    loggingNatchez.js,
    loggingConfig,
  )
  .settings(
    name := "logging",
  )

lazy val loggingCore = (crossProject(JVMPlatform, JSPlatform) in file("logging-core"))
  .settings(crossDependencies(
    peknight.ext.log4Cats,
    peknight.error,
    typelevel.catsEffect,
  ))
  .settings(
    name := "logging-core",
  )

lazy val loggingNatchez = (crossProject(JVMPlatform, JSPlatform) in file("logging-natchez"))
  .dependsOn(loggingCore)
  .settings(crossDependencies(tpolecat.natchez))
  .settings(
    name := "logging-natchez",
  )

lazy val loggingConfig = (project in file("logging-config"))
  .aggregate(
    logbackConfig.jvm,
    logbackConfig.js,
    logbackConfig.native,
  )
  .settings(
    name := "logging-config",
  )

lazy val logbackConfig = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("logging-config/logback-config"))
  .settings(
    name := "logback-config",
    libraryDependencies ++= Seq(
    ),
  )
