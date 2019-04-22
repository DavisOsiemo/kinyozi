name := "barber_salon"

version := "0.1"

scalaVersion := "2.12.8"

lazy val sharedDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.16",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.16",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.16" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.16",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.16" % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",
  "io.spray"          %%  "spray-json" % "1.3.4",
  "net.debasishg"     %% "redisclient" % "3.8",
  "org.slf4j"         % "slf4j-simple" % "1.6.4",
  "org.scala-debugger"  %% "scala-debugger-api" % "1.1.0-M3",
  "org.json"          % "json"   % "20180813",
  "mysql"             % "mysql-connector-java" % "5.1.24",
  "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"
)

lazy val kinyozi = (project in file("kinyozi")).
  settings(
    libraryDependencies ++= sharedDependencies
  )
