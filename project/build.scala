import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq(
    name := "ColumnLayout",
    version := "0.1",
    scalaVersion := "2.9.1",
    useProguard in Android := false,
    platformName in Android := "android-14"
  )

  lazy val fullAndroidSettings =
    General.settings ++
      AndroidProject.androidSettings ++
      TypedResources.settings ++
      AndroidMarketPublish.settings ++ Seq(
      keyalias in Android := "change-me",
      useProguard in Android := false,
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"
    )
}

object AndroidBuild extends Build {
  lazy val main = Project(
    "ColumnLayout",
    file("."),
    settings = General.fullAndroidSettings
  )

  lazy val tests = Project(
    "tests",
    file("src/it"),
    settings = General.settings ++ AndroidTest.androidSettings ++ Seq(
      useProguard in Android := false,
      libraryDependencies += "org.specs2" %% "specs2" % "1.7.1"    ,
      name := "ColumnLayoutTests"
    )
  ) dependsOn main
}
