name := "tidori"
lazy val lastVersion = "0.0.2"

lazy val commonSettings = Seq(
  version := lastVersion,
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
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += Resolver.sonatypeRepo("releases"),
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
  .settings(
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    pomExtra :=
      <url>https://github.com/ariwaranosai/scala-js-hashes.git</url>
        <scm>
          <url>git@github.com:ariwaranosai/tidori.git</url>
          <connection>scm:git:git@github.com:ariwaranosai/tidori.git</connection>
        </scm>
        <developers>
          <developer>
            <id>ariwaranosai</id>
            <name>ariwaranosai</name>
            <url>http://ariwaranosai.xyz</url>
          </developer>
        </developers>
  )

lazy val example = (project in file("examples"))
  .settings(commonSettings:_*)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    resolvers += Resolver.bintrayRepo("ariwarasai","maven"),
    libraryDependencies += "xyz.ariwaranosai" %%% "tidori" % lastVersion
  )

lazy val macros = (project in file("tidorim"))
  .settings(commonSettings:_*)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalacOptions ++= Seq (
      // "-Ymacro-debug-lite"
    ),
    libraryDependencies ++= Seq(
      scalaVersion("org.scala-lang" % "scala-reflect" % _).value,
      scalaVersion("org.scala-lang" % "scala-compiler" % _).value,
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
    )
  ).dependsOn(tidori)

lazy val mexample = (project in file("mexamples"))
  .settings(commonSettings:_*)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq (
      scalaVersion("org.scala-lang" % "scala-reflect" % _).value,
      scalaVersion("org.scala-lang" % "scala-compiler" % _).value,
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
    )
  ).dependsOn(macros).dependsOn(tidori)