organization := "de.fashionid.anorm"
name := "functional-anorm"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "anorm" % "2.5.3",
  "com.zaxxer" % "HikariCP" % "2.4.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.hsqldb" % "hsqldb" % "2.3.5" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
