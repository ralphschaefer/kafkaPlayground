organization := "my.kafkaDemo"

scalaVersion := "2.13.3"

version := "0.1"

name := "kafkaDemo"


lazy val producer = (project in file("producer")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.13.3",
      organization := "my.kafkaDemo",
      test in assembly := {},
      assemblyJarName in assembly := "kafkaDemoProducer.jar"
    )),
    name := "kafkaDemoProducer",
    assemblyJarName in assembly := name.value + ".jar",
    test in assembly := {},
    libraryDependencies ++=
      KafkaDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.json4s" %% "json4s-jackson" % "3.6.9"
    )
  )

lazy val consumer = (project in file("consumer")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.13.3",
      organization := "my.kafkaDemo",
      test in assembly := {},
      assemblyJarName in assembly := "kafkaDemoConsumer.jar"
    )),
    name := "kafkaDemoConsumer",
    assemblyJarName in assembly := name.value + ".jar",
    test in assembly := {},
    libraryDependencies ++=
      KafkaDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.json4s" %% "json4s-jackson" % "3.6.9"
    )
  )