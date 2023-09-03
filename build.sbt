ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "cardigan-prep"
  )

libraryDependencies += "co.fs2" %% "fs2-core" % "3.9.1"
libraryDependencies += "co.fs2" %% "fs2-io" % "3.9.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % "0.14.1")
