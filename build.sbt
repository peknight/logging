import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val logging = (project in file("."))
  .settings(name := "logging")
  .aggregate(
    loggingCore.jvm,
    loggingCore.js,
    loggingNatchez.jvm,
    loggingNatchez.js,
    loggingConfig,
  )

lazy val loggingCore = (crossProject(JVMPlatform, JSPlatform) in file("logging-core"))
  .settings(name := "logging-core")
  .settings(crossDependencies(
    peknight.log4Cats,
    peknight.error,
    typelevel.catsEffect,
  ))

lazy val loggingNatchez = (crossProject(JVMPlatform, JSPlatform) in file("logging-natchez"))
  .dependsOn(loggingCore)
  .settings(name := "logging-natchez")
  .settings(crossDependencies(tpolecat.natchez))

lazy val loggingConfig = (project in file("logging-config"))
  .settings(name := "logging-config")
  .aggregate(
    logbackConfig.jvm,
    logbackConfig.js,
    logbackConfig.native,
  )

lazy val logbackConfig = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("logging-config/logback-config"))
  .settings(name := "logback-config")
