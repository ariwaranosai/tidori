name := "tidori"

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  organization := "xyz.ariwaranosai",
  scalacOptions ++= Seq(
    "-language:implicitConversions",
    "-language:existentials",
    "-Xlint",
    "-deprecation",
    "-Xfatal-warnings",
    "-feature",
    "-language:postfixOps"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "com.lihaoyi" %%% "scalatags" % "0.6.1",
    "com.lihaoyi" %%% "utest" % "0.4.3" % "test"
  ),
  jsEnv := PhantomJSEnv(args = Seq("--web-security=no", "")).value,
  jsDependencies ++= Seq(
    RuntimeDOM % "test"
  )
)

lazy val tidori = (project in file("."))
  .settings(commonSettings:_*)
  .enablePlugins(ScalaJSPlugin)

lazy val example = (project in file("examples"))
  .settings(commonSettings:_*)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(tidori)
