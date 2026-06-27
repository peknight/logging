import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val logging = (project in file("."))
  .settings(name := "logging")
  .aggregate(loggingCore.projectRefs *)
  .aggregate(loggingNatchez.projectRefs *)
  .aggregate(loggingConfig)

lazy val loggingCore = (projectMatrix in file("logging-core"))
  .settings(name := "logging-core")
  .settings(libraryDependencies ++= dependencies(
    peknight.log4Cats,
    peknight.error,
    typelevel.catsEffect,
  ))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))

lazy val loggingNatchez = (projectMatrix in file("logging-natchez"))
  .dependsOn(loggingCore)
  .settings(name := "logging-natchez")
  .settings(libraryDependencies ++= dependencies(tpolecat.natchez))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))

lazy val loggingConfig = (project in file("logging-config"))
  .settings(name := "logging-config")
  .aggregate(logbackConfig.projectRefs *)

lazy val logbackConfig = (projectMatrix in file("logging-config/logback-config"))
  .settings(name := "logback-config")
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
  .nativePlatform(scalaVersions = Seq(scala.scala3.version))
