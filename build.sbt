import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "polyunit",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.5.0" % Test,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0",
    libraryDependencies += "org.typelevel" %% "cats-laws" % "2.3.0" % Test,
    libraryDependencies += "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
    libraryDependencies += "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.3" % Test,
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
