lazy val commonSettings = Seq(
  organization := "de.dangoe",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.2",
  crossScalaVersions := Seq("2.11.8", "2.12.2"),
  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation"),
  updateOptions := updateOptions.value.withCachedResolution(true),
  libraryDependencies ++= Seq(
    "org.hsqldb" % "hsqldb" % "2.4.0" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test"
  )
)

lazy val root = (project in file("."))
  .aggregate(core, `hikari-support`)
  .settings(
    commonSettings,
    name := "freda-parent"
  )

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    name := "freda-core"
  )

lazy val `anorm-support` = (project in file("anorm-support"))
  .settings(
    commonSettings,
    name := "freda-anorm-support",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "anorm" % "2.6.0-M1"
    )
  ) dependsOn core

lazy val `hikari-support` = (project in file("hikari-support"))
  .settings(
    commonSettings,
    name := "freda-hikari-support",
    libraryDependencies ++= Seq(
      "com.zaxxer" % "HikariCP" % "2.6.3"
    )
  ) dependsOn core
