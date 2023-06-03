ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Connection"
  )


libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "16.0.0-R23",
  "org.openjfx" % "javafx-controls" % "19.0.2.1",
  "org.openjfx" % "javafx-fxml" % "19.0.2.1",
  "org.openjfx" % "javafx-media" % "19.0.2.1",
  "mysql" % "mysql-connector-java" % "8.0.32"
)