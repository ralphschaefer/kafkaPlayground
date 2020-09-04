import sbt._

object KafkaDependencies {
  val kafkaVersion       = "2.6.0"
  val avro4sVersion      = "3.0.9"

  lazy val libs = Seq(
    "org.apache.kafka" % "kafka-clients" % kafkaVersion,
    "com.sksamuel.avro4s" %% "avro4s-core" % avro4sVersion
  )
}

