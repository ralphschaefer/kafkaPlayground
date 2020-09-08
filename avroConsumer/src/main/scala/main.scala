
import java.util.Properties
import org.apache.kafka.clients.consumer._
import scala.concurrent.duration._
import scala.jdk.DurationConverters._
import scala.jdk.CollectionConverters._
import ch.qos.logback.classic.{Level,Logger}
import org.slf4j.LoggerFactory
// import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord

object main extends App {

  LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)

  println("AvroConsumer")

  val topic = "testtMyTest"

  val noMessageMaxTicks = 200*2

  val kafkaProps = new Properties()
  kafkaProps.put("bootstrap.servers", "localhost:9092")
  kafkaProps.put("group.id", "group2" )
  kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
  kafkaProps.put("schema.registry.url", "http://172.166.1.22:8081")
  kafkaProps.put("max.poll.records", 1)

  val consumer = new KafkaConsumer[String, GenericRecord](kafkaProps)
  consumer.subscribe(List(topic).asJava)

  var noMessagesCount = 0

  while (noMessagesCount < noMessageMaxTicks) {
    // println("----- polling ...")
    val consumerRecords = consumer.poll(500.millis.toJava)
    if (consumerRecords.count() > 0) {
      println(s"----- records found: ${consumerRecords.count()}")
      consumerRecords.forEach { record =>
        val item:GenericRecord = record.value()
        val name = item.get("name")
        val id = item.get("id")
        val ts = item.get("ts")
        println(s"----- record(${record.key()}) = $id:$name/$ts [offset: ${record.offset()}, partition: ${record.partition()}]")
      }
      consumer.commitAsync()
      noMessagesCount = 0
    } else {
      noMessagesCount += 1
      // println(s"----- waiting for another ${noMessageMaxTicks-noMessagesCount}-Ticks for messages ")
    }
  }
  println("----- closing")
  consumer.close()

}
