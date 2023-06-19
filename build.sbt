ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.2.7"
lazy val root = (project in file("."))
  .enablePlugins(AkkaGrpcPlugin)
  .settings(
    name := "responsibly-test",
    akkaGrpcGeneratedSources := Seq(AkkaGrpc.Server, AkkaGrpc.Client),
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature"),
    javaOptions += "-Djava.net.preferIPv4Stack=true",
    libraryDependencies ++= {
      Seq(
        "org.tpolecat" %% "doobie-core" % "0.9.0",
        "org.tpolecat" %% "doobie-postgres" % "0.9.0",
        "org.tpolecat" %% "doobie-hikari" % "0.9.0",
        "org.wvlet.airframe" %% "airframe-log" % "20.12.1",
        "org.flywaydb" % "flyway-core" % "8.0.3",
        "org.postgresql" % "postgresql" % "42.6.0",
        "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
        "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
        "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
        "com.lightbend.akka.grpc" %% "akka-grpc-runtime" % "2.3.2",
        "org.scalatest" %% "scalatest" % "3.2.16" % Test
      )
    }
  )
