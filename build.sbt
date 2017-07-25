organization := "de.dangoe"
name := "freda"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.1"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "anorm" % "2.6.0-M1",
  "com.zaxxer" % "HikariCP" % "2.6.3",
  "org.hsqldb" % "hsqldb" % "2.4.0" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
