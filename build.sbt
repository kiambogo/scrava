name := "Scrava"

organization := "com.github.kiambogo"

version := Process("git describe --dirty --tags --abbrev=0 --always", baseDirectory.value).!!.stripPrefix("v").trim

scalaVersion := "2.12.1"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "net.liftweb" %% "lift-json" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8"
)

pomExtra :=
  <url>https://github.com/kiambogo/scrava</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>http://www.opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:kiambogo/scrava.git</url>
    <connection>scm:git:git@github.com:kiambogo/scrava.git</connection>
  </scm>
  <developers>
    <developer>
      <id>kiambogo</id>
      <name>Christopher Poenaru</name>
      <url>http://chrispoenaru.com</url>
    </developer>
  </developers>
