name := "Scrava"

organization := "kiambogo"

version := Process("git describe --dirty --tags --always", baseDirectory.value).!!.stripPrefix("v").trim

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "net.liftweb" %% "lift-json" % "3.0-M2",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "joda-time" % "joda-time" % "2.9.4",
  "org.joda" % "joda-convert" % "1.8"
)
