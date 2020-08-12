import sbt._

object KafkaDependencies {
  val kafkaVersion       = "2.6.0"

  lazy val libs = Seq(
    "org.apache.kafka" % "kafka-clients" % kafkaVersion
  )
}

