name := """Scrava"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "net.liftweb" %% "lift-json" % "3.0-M2",
  "org.scalatest" %% "scalatest" % "2.2.2"
)
