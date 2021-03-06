name := "scala-log"

scalaVersion := "2.10.5"

scalacOptions in ThisBuild ++= Seq(
    "-deprecation",
    "-feature",
    "-language:postfixOps",
    "-unchecked"
)

resolvers ++= Seq(
    "sbt-plugin-releases" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"
)

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")