import com.typesafe.sbt.pgp.PgpKeys
import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease._
import xerial.sbt.Sonatype._

object TheBuild extends Build {

    lazy val scalaLog = Project("scala-log", file(".")).
        settings(
            organization := "com.github.bespalovdn",
            name := "scala-log",
            description := "Logging facilities for Scala projects."
        ).
        settings(releaseSettings:_*).
        settings(sonatypeSettings:_*).
        settings(publishSettings:_*)

    def publishSettings: Seq[Setting[_]] = Seq(
        // If we want on maven central, we need to be in maven style.
        publishMavenStyle := true,
        publishArtifact in Test := false,
        // The Nexus repo we're publishing to.
        publishTo := {
            val nexus = "https://oss.sonatype.org/"
            if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
            else Some("releases"  at nexus + "service/local/staging/deploy/maven2")
        },
        // Maven central cannot allow other repos.  We're ok here because the artifacts we
        // we use externally are *optional* dependencies.
        pomIncludeRepository := { _ => false },
        // Maven central wants some extra metadata to keep things 'clean'.
        homepage := Some(url("https://github.com/bespalovdn/scala-log")),
        licenses += "BSD-Style" -> url("http://www.opensource.org/licenses/bsd-license.php"),
        scmInfo := Some(ScmInfo(url("https://github.com/bespalovdn/scala-log"), "scm:git@github.com:bespalovdn/scala-log.git")),
        pomExtra := (
            <developers>
                <developer>
                    <id>bespalovdn</id>
                    <name>Dmitry Bespalov</name>
                    <email>bespalovdn@gmail.com/</email>
                </developer>
            </developers>
        ),
        ReleasePlugin.ReleaseKeys.releaseProcess := Seq[ReleaseStep](
            checkSnapshotDependencies,
            inquireVersions,
            runClean,
            runTest,
            setReleaseVersion,
            commitReleaseVersion,
            tagRelease,
            ReleaseStep(
                action = { state =>
                    val extracted = Project extract state
                    extracted.runAggregated(PgpKeys.publishSigned in Global in extracted.get(thisProjectRef), state)
                },
                enableCrossBuild = true
            ),
            ReleaseStep{ state =>
                val extracted = Project extract state
                extracted.runAggregated(SonatypeKeys.sonatypeReleaseAll in Global in extracted.get(thisProjectRef), state)
            },
            setNextVersion,
            commitNextVersion,
            pushChanges
        )
    )
}
