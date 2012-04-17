import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq(
    name := "ColumnLayout",
    version := "0.1",
    scalaVersion := "2.9.1",
    useProguard in Android := false,
    platformName in Android := "android-14",

    resolvers ++= Seq(
      "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "releases" at "http://oss.sonatype.org/content/repositories/releases"
    )
  )

  lazy val fullAndroidSettings =
    General.settings ++
      AndroidProject.androidSettings ++
      TypedResources.settings ++ Seq(
      useProguard in Android := false,
      libraryDependencies += "com.google.android" % "support-v4" % "r6",
      fullClasspath in Test ~= {
        (cp: Classpath) =>
          cp.sortWith((a, b) => !a.data.toString.contains("android"))
      }
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
    settings = General.fullAndroidSettings ++ Seq(
      useProguard in Android := false,
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "1.8-SNAPSHOT",
        "com.google.android" % "support-v4" % "r6"
      ),
      instrumentationRunner in Android := "org.scalatest.tools.SpecRunner",
      dxInputs in Android ~= {
        (inputs: Seq[File]) =>
          inputs.filterNot(n => n.getName.contains("specs") || n.getName.contains("scalatest"))
      },
      name := "ColumnLayoutTests"
    )
  ) dependsOn main
}


