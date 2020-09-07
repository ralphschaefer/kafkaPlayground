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
      // "org.json4s" %% "json4s-jackson" % "3.6.9"
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
      // "org.json4s" %% "json4s-jackson" % "3.6.9"
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
  consumer, producer
).map(_.id)

addCommandAlias("cleanAll", allProjects.map(name => s"; $name/clean").mkString)

addCommandAlias("updateAll", allProjects.map(name => s"; $name/update").mkString)

addCommandAlias("compileAll", allProjects.map(name => s"; $name/compile").mkString)

addCommandAlias("assemblyAll", allProjects.map(name => s"; $name/assembly").mkString)

assemblyMergeStrategy in assembly := {
  case "module-info.class" => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x if Assembly.isConfigFile(x) => MergeStrategy.concat
  case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.discard
  case PathList("META-INF", xss @ _*) => xss map {_.toLowerCase} match {
    case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
    case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") => MergeStrategy.discard
    case  ps@(x :: xs) if ps.last.endsWith("pom.xml") => MergeStrategy.discard
    case  ps@(x :: xs) if ps.last.endsWith("pom.properties") => MergeStrategy.discard
    case _ => MergeStrategy.deduplicate
  }
  case PathList("schemaorg_apache_xmlbeans", xs @ _*) => MergeStrategy.first
  case "asm-license.txt" | "overview.html" => MergeStrategy.discard
  // case "javax/servlet/jsp/resources/jsp_2_0.xsd" => MergeStrategy.discard
  // case _ => MergeStrategy.deduplicate
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}