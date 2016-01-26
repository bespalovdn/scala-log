name := "scala-logger-itf_2.10"

scalaVersion := "2.10.5"

scalacOptions in ThisBuild ++= Seq(
    "-deprecation",
    "-feature",
    "-language:postfixOps",
    "-unchecked"
)

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"
