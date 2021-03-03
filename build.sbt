name := "Scrava"

organization := "com.github.kiambogo"

scalaVersion := "2.13.5"

crossScalaVersions := Seq("2.11.8", "2.12.3")

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
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "io.argonaut" %% "argonaut" % "6.3.3",
  "org.scalatest" %% "scalatest" % "3.2.5" % "test",
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
