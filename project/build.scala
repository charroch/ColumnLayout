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
      "snapshots" at "http://scala-tools.org/repo-snapshots",
      "releases" at "http://scala-tools.org/repo-releases"
    )
  )

  lazy val fullAndroidSettings =
    General.settings ++
      AndroidProject.androidSettings ++
      TypedResources.settings ++ Seq(
      useProguard in Android := false,

      libraryDependencies ++= Seq(
        "com.pivotallabs" % "robolectric" % "1.0" % "test",
        "junit" % "junit-dep" % "4.8.2" % "test",
        "org.specs2" %% "specs2" % "1.8-SNAPSHOT" % "test",
        "org.mockito" % "mockito-all" % "1.9.0" % "test"
      ),

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
    settings = General.settings ++ AndroidTest.androidSettings ++ AndroidTest.settings ++ Seq(
      useProguard in Android := false,
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "1.7.1",
        "org.specs2" %% "specs2" % "1.9-SNAPSHOT"
      ),
      instrumentationRunner in Android := "org.scalatest.tools.SpecRunner",
      dxInputs in Android ~= {
        (inputs: Seq[File]) =>
          inputs.filterNot(_.getName.contains("scalatest"))
      },
      name := "ColumnLayoutTests"
    )
  ) dependsOn main
}


