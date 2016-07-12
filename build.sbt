import scalariform.formatter.preferences._

name := """bbs_ddd_play"""

version := "1.0-SNAPSHOT"

lazy val currentScalaVersion = "2.11.7"

lazy val mysqlVersion = "5.1.34"
lazy val scalikejdbcVersion = "2.2.8"
lazy val scalikejdbcPluginVersion = "2.4.2"

scalacOptions ++= Seq(
  "utf8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Xfatal-warnings"
)

lazy val commonSettings = scalariformSettings ++ Seq(
  scalaVersion := currentScalaVersion,
  libraryDependencies ++= Seq(
    jdbc,
    cache,
    ws,
    specs2 % Test,
    "mysql" % "mysql-connector-java" % mysqlVersion
  ),
  compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
  (compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle,
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, false)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesWithinPatternBinders, true)
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .aggregate(application, domain, infrastructure)
  .settings(
    run := {
      (run in application in Compile).evaluated
    }
  )

// Scala style
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

lazy val application = Project(
  id = "application",
  base = file("application")
).dependsOn(domain, infrastructure)
  .enablePlugins(PlayScala).settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      evolutions
    ),
    routesGenerator := InjectedRoutesGenerator

  )

lazy val domain = Project(
  id = "domain",
  base = file("domain")
).dependsOn(infrastructure).settings(commonSettings: _*)
  .settings(
    scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala",
    scalaSource in Test := baseDirectory.value / "src" / "test" / "scala",
    parallelExecution in Test := false
  )

lazy val infrastructure = Project(
  id = "infrastructure",
  base = file("infrastructure")
).settings(commonSettings: _*).settings(
    scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala",
    scalaSource in Test := baseDirectory.value / "src" / "test" / "scala",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % "test",
      "org.scalikejdbc" %% "scalikejdbc-play-initializer" % scalikejdbcPluginVersion,
      "org.scalikejdbc" %% "scalikejdbc-play-fixture" % scalikejdbcPluginVersion
    ),
    parallelExecution in Test := false
  )


