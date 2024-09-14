version := "0.1.0-SNAPSHOT"

name := "Scala-Fire-Tower"
scalaVersion := "3.4.2"

fork := true
fork / run := true

semanticdbEnabled := true

lazy val ScalaLibraryVersion       = "3.4.2"
lazy val ScalaTestVersion          = "3.2.15"
lazy val ScalaTestPlusJUnitVersion = "3.2.15.0"
lazy val MonixVersion              = "3.4.1"
lazy val ScalaFxVersion            = "21.0.0-R32"
lazy val LogbackVersion            = "1.2.10"
lazy val ScalaLoggingVersion       = "3.9.4"
lazy val TuprologVersion           = "4.1.1"
lazy val ScoveragePluginVersion    = "8.1"
lazy val ScalafixPluginVersion     = "0.2.2"
lazy val JavaFxPluginVersion       = "0.0.13"
lazy val CirceYamlVersion          = "1.15.0"
lazy val CirceGenericVersion       = "0.14.9"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging"  % ScalaLoggingVersion,
  "ch.qos.logback"             % "logback-classic" % LogbackVersion,
  "org.scalafx"                %% "scalafx"        % ScalaFxVersion,
  "io.monix"                   %% "monix"          % MonixVersion,
  "org.scalatest"              %% "scalatest"      % ScalaTestVersion % Test,
  "org.scalatestplus"          %% "junit-4-13"     % ScalaTestPlusJUnitVersion % Test,
  "io.circe"                   %% "circe-yaml"     % CirceYamlVersion,
  "it.unibo.alice.tuprolog"    % "2p-core"         % TuprologVersion,
  "io.circe"                   %% "circe-generic"  % CirceGenericVersion
)

//from https://tpolecat.github.io/2017/04/25/scalac-flags.html
scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn if an import selector is not referenced.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:params", // Warn if a value parameter is unused.
  //"-Wunused:privates", // Warn if a private member is unused.
  //"-Wvalue-discard" // Warn when non-Unit expression results are unused.
)

scalafmtOnCompile := true

enablePlugins(SitePreviewPlugin, AsciidoctorPlugin)
enablePlugins(SiteScaladocPlugin)
SiteScaladoc / siteSubdirName := "api/latest"

Asciidoctor / sourceDirectory := sourceDirectory.value / "asciidoc"
Asciidoctor / siteSubdirName := "asciidoc"

enablePlugins(GhpagesPlugin)

git.remoteRepo := "git@github.com:Fight-Fire-With-Scala/Scala-Fire-Tower.git"

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _                        => MergeStrategy.first
}

assembly / assemblyJarName := "scala-fire-tower.jar"
