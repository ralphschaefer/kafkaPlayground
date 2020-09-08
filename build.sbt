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
    resolvers += "confluent" at "https://packages.confluent.io/maven/",
    libraryDependencies ++=
      KafkaDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.json4s" %% "json4s-jackson" % "3.6.9",
      "com.github.javafaker" % "javafaker" % "1.0.2"
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
    resolvers += "confluent" at "https://packages.confluent.io/maven/",
    libraryDependencies ++=
      KafkaDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.json4s" %% "json4s-jackson" % "3.6.9"
    )
  )

lazy val avroConsumer = (project in file("avroConsumer")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.13.3",
      organization := "my.kafkaDemo",
      test in assembly := {},
      assemblyJarName in assembly := "avroConsumer.jar"
    )),
    name := "avroConsumer",
    assemblyJarName in assembly := name.value + ".jar",
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case "module-info.class" => MergeStrategy.concat
      case s => MergeStrategy.defaultMergeStrategy(s)
    },
    resolvers += "confluent" at "https://packages.confluent.io/maven/",
    libraryDependencies ++=
      KafkaDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.json4s" %% "json4s-jackson" % "3.6.9",
      "org.apache.avro" % "avro" % "1.10.0",
      ("io.confluent" % "kafka-avro-serializer" % "5.5.1")// .exclude("javax.ws.rs", "javax.ws.rs-api")
      //"javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" % "provided"
    )
  )

lazy val avroProducer = (project in file("avroProducer")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.13.3",
      organization := "my.kafkaDemo",
      test in assembly := {},
      assemblyJarName in assembly := "avroProducer.jar"
    )),
    name := "avroProducer",
    assemblyJarName in assembly := name.value + ".jar",
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case "module-info.class" => MergeStrategy.concat
      case s => MergeStrategy.defaultMergeStrategy(s)
    },
    resolvers ++= Seq(
      "confluent" at "https://packages.confluent.io/maven/",
      "jitpack.io" at "https://jitpack.io"
    ),
    libraryDependencies ++=
      KafkaDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      // "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "org.json4s" %% "json4s-jackson" % "3.6.9",
      "org.apache.avro" % "avro" % "1.10.0",
      "com.github.javafaker" % "javafaker" % "1.0.2",
      "io.confluent" % "kafka-schema-registry" % "5.5.1",
      // "io.confluent" % "kafka-streams-avro-serde" % "5.5.1",
      "io.confluent" % "kafka-avro-serializer" % "5.5.1" // .exclude("javax.ws.rs", "javax.ws.rs-api")
      //"javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" % "provided"
    )
  )

lazy val simpleREST = (project in file("simpleREST")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.13.3",
      organization := "my.kafkaDemo",
      test in assembly := {},
      assemblyJarName in assembly := "simpleREST.jar"
    )),
    name := "simpleREST",
    assemblyJarName in assembly := name.value + ".jar",
    assemblyMergeStrategy in assembly := {
      case "module-info.class" => MergeStrategy.concat
      case s => MergeStrategy.defaultMergeStrategy(s)
    },
    test in assembly := {},
    libraryDependencies ++=
      CatsDependencies.libs ++
      Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.sksamuel.avro4s" %% "avro4s-core" % "4.0.0"
    )
  )

val allProjects=Seq(
  consumer, producer, simpleREST, avroConsumer, avroProducer
).map(_.id)

addCommandAlias("cleanAll", allProjects.map(name => s"; $name/clean").mkString)

addCommandAlias("updateAll", allProjects.map(name => s"; $name/update").mkString)

addCommandAlias("compileAll", allProjects.map(name => s"; $name/compile").mkString)

addCommandAlias("assemblyAll", allProjects.map(name => s"; $name/assembly").mkString)
