name := "Scrava"

organization := "kiambogo"

version := "1.1.3"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "1.1.0",
  "net.liftweb" %% "lift-json" % "3.0-M2",
  "org.scalatest" %% "scalatest" % "2.2.2",
  "joda-time" % "joda-time" % "2.6",
  "org.joda" % "joda-convert" % "1.2"
)
