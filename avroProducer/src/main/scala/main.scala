
import java.util.{Locale, Properties}

import org.apache.kafka.clients.consumer._

import scala.concurrent.duration._
import scala.jdk.DurationConverters._
import scala.jdk.CollectionConverters._
import ch.qos.logback.classic.{Level, Logger}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory
// import io.confluent.kafka.serializers.KafkaAvroDeserializer
import com.github.javafaker.Faker

import org.apache.avro.generic.GenericRecord

object main extends App {

  LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)
  val faker = new Faker(new Locale("en"))
  val count = args(0).toInt

  println("Avro Producer")


  val schema = "{\"type\":\"record\",\"name\":\"Echo\",\"namespace\":\"my.simpleREST.Messages\",\"fields\":[{\"name\":\"msg\",\"type\":\"string\"}]}"
  val avroSchema = new Schema.Parser().parse(schema)

  def buildRecord(msg:String) = {
    val record = new GenericData.Record(avroSchema)
    record.put("msg", msg)
    record
  }

  val topic = "jdbcpipe"
  val registry = "http://172.166.1.22:8081"


  val kafkaProps = new Properties()
  kafkaProps.put("bootstrap.servers",     "172.166.1.21:9092");
  kafkaProps.put("key.serializer",        "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer",      "io.confluent.kafka.serializers.KafkaAvroSerializer")
  kafkaProps.put("schema.registry.url",   registry)

  val producer = new KafkaProducer[String, GenericRecord](kafkaProps)

  for( i <- 1 to count) {
    try {
      val record = new ProducerRecord[String, GenericRecord](
        topic, null, buildRecord(faker.lordOfTheRings().character() + " agrees that: " + "'" + faker.chuckNorris().fact() + "'")
      )
      producer.send(record)
      println(s"----- send record: $i")
    } catch {
      case e:Throwable => println(s"----- send failed: ${e.getMessage}")
    }
  }
  producer.flush()

}
