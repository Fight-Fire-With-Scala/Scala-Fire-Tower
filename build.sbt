version := "0.1.0-SNAPSHOT"

name := "Scala-Fire-Tower"
scalaVersion := "3.4.2"

fork := true
fork / run := true

lazy val ScalaLibraryVersion = "3.4.2"
lazy val ScalaTestVersion = "3.2.15"
lazy val ScalaTestPlusJUnitVersion = "3.2.15.0"
lazy val MonixVersion = "3.4.1"
lazy val ScalaFxVersion = "21.0.0-R32"
lazy val LogbackVersion = "1.2.10"
lazy val ScalaLoggingVersion = "3.9.4"
lazy val TuprologVersion = "4.1.1"
lazy val ScoveragePluginVersion = "8.1"
lazy val ScalafixPluginVersion = "0.2.2"
lazy val JavaFxPluginVersion = "0.0.13"
lazy val CirceYamlVersion = "1.15.0"
lazy val CirceGenericVersion = "0.14.9"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  "org.scalafx" %% "scalafx" % ScalaFxVersion,
  "io.monix" %% "monix" % MonixVersion,
  "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
  "org.scalatestplus" %% "junit-4-13" % ScalaTestPlusJUnitVersion % Test,
  "io.circe" %% "circe-yaml" % CirceYamlVersion,
  "it.unibo.alice.tuprolog" % "2p-core" % TuprologVersion,
  "io.circe" %% "circe-generic" % CirceGenericVersion
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-language:implicitConversions")

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

assembly / assemblyJarName := "ex1.jar"