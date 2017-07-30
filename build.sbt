lazy val scalatestVersion = "3.0.1"

lazy val commonSettings = Seq(
  organization := "de.dangoe",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.3",
  crossScalaVersions := Seq("2.11.8", "2.12.3"),
  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation"),
  updateOptions := updateOptions.value.withCachedResolution(true),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test"
  )
)

lazy val root = (project in file("."))
  .aggregate(core, anorm, jooq, `hikari-support`, testapp, testsupport)
  .settings(
    commonSettings,
    name := "freda-parent"
  )

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    name := "freda-core"
  )

lazy val `anorm` = (project in file("anorm"))
  .settings(
    commonSettings,
    name := "freda-anorm",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "anorm" % "2.6.0-M1"
    )
  ) dependsOn(core, testsupport % "test->compile")

lazy val `jooq` = (project in file("jooq"))
  .settings(
    commonSettings,
    name := "freda-jooq",
    libraryDependencies ++= Seq(
      "org.jooq" % "jooq-scala" % "3.9.4",
      "org.jooq" % "jooq-codegen" % "3.9.4"
    )
  ) dependsOn(core, testsupport % "test->compile")

lazy val `hikari-support` = (project in file("hikari-support"))
  .settings(
    commonSettings,
    name := "freda-hikari-support",
    libraryDependencies ++= Seq(
      "com.zaxxer" % "HikariCP" % "2.6.3"
    )
  ) dependsOn core

lazy val `testapp` = (project in file("testapp"))
  .settings(
    commonSettings,
    name := "freda-testapp",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.1.3"
    )
  ) dependsOn(core, anorm, jooq, `hikari-support`)

lazy val `testsupport` = (project in file("testsupport"))
  .settings(
    commonSettings,
    name := "freda-testsupport",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalatestVersion,
      "org.hsqldb" % "hsqldb" % "2.4.0"
    )
  ) dependsOn core
