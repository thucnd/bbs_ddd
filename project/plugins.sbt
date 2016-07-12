// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.3")

// Scalastyle plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

// scalainfor plugin
addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")
