lazy val commonSettings = Seq(
  organization := "de.dangoe",
  version := "1.0.0",
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.11.8", "2.12.3"),
  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature"),
  updateOptions := updateOptions.value.withCachedResolution(true),
  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-log4j12" % "1.7.25" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test"
  )
)

lazy val root = (project in file(".")).
  aggregate(core, anorm, `hikari-support`, testsupport).
  settings(
    commonSettings,
    name := "freda-parent"
  )

lazy val core = (project in file("core")).
  settings(
    commonSettings,
    name := "freda-core"
  )

lazy val `anorm` = (project in file("anorm")).
  settings(
    commonSettings,
    name := "freda-anorm",
    libraryDependencies ++= Seq(
      "org.playframework.anorm" %% "anorm" % "2.6.0"
    )
  ).dependsOn(core, testsupport % "test->compile")

lazy val `hikari-support` = (project in file("hikari-support")).
  settings(
    commonSettings,
    name := "freda-hikari-support",
    libraryDependencies ++= Seq(
      "com.zaxxer" % "HikariCP" % "2.6.3"
    )
  ).dependsOn(core, testsupport % "test->compile")

lazy val testsupport = (project in file("testsupport")).
  settings(
    commonSettings,
    name := "freda-testsupport",
    libraryDependencies ++= Seq(
      "org.hsqldb" % "hsqldb" % "2.4.0"
    )
  ).dependsOn(core)
