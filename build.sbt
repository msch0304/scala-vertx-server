import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "de.michaschmidt",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "scala-vertx-server",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "io.vertx" %% "vertx-lang-scala" % "3.5.1",
    libraryDependencies += "io.vertx" %% "vertx-web-scala" % "3.5.1",
    mainClass in (Compile, run) := Some("de.michaschmidt.vertx.server.Starter")
  )
