
import java.util.Properties
import org.apache.kafka.clients.consumer._
import scala.concurrent.duration._
import scala.jdk.DurationConverters._
import scala.jdk.CollectionConverters._
import ch.qos.logback.classic.{Level,Logger}
import org.slf4j.LoggerFactory

object main extends App {

  LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)

  println("Consumer")

  val topic = "testtopic"

  val noMessageMaxTicks = 20*2

  val kafkaProps = new Properties()
  kafkaProps.put("bootstrap.servers", "localhost:9092")
  kafkaProps.put("group.id", "consumer_grp1" )
  kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put("max.poll.records", 3)
  val consumer = new KafkaConsumer[String, String](kafkaProps)
  consumer.subscribe(List(topic).asJava)

  var noMessagesCount = 0

  while (noMessagesCount < noMessageMaxTicks) {
    println("----- polling ...")
    val consumerRecords = consumer.poll(500.millis.toJava)
    println(s"----- records found: ${consumerRecords.count()}")
    if (consumerRecords.count() > 0) {
      consumerRecords.forEach { record =>
        println(s"----- record(${record.key()}) = ${record.value()} [offset: ${record.offset()}, partition: ${record.partition()}]")
      }
      consumer.commitAsync()
      noMessagesCount = 0
    } else {
      noMessagesCount += 1
      println(s"----- waiting for another ${noMessageMaxTicks-noMessagesCount}-Ticks for messages ")
    }
  }
  println("----- closing")
  consumer.close()

}
