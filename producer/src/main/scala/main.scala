
import java.util.{Locale, Properties}

import org.apache.kafka.clients.producer._
import ch.qos.logback.classic.{Level, Logger}
import org.slf4j.LoggerFactory
import com.github.javafaker.Faker

object main extends App {

    LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)
    val faker = new Faker(new Locale("de"))

    val count = args(0).toInt

    println(s"Producer for $count")

    val topic = "testtopic"

    val kafkaProps = new Properties()
    kafkaProps.put("bootstrap.servers",     "172.166.1.21:9092");
    kafkaProps.put("key.serializer",        "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProps.put("value.serializer",      "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](kafkaProps)
    for( i <- 1 to count) {
        try {
            val record = new ProducerRecord[String, String](topic, null, s"$i / ${faker.name().fullName()} / ${faker.address().fullAddress()} ")
            producer.send(record)
            println(s"----- send record: $i")
        } catch {
            case e:Throwable => println(s"----- send failed: ${e.getMessage}")
        }
    }
    producer.flush()
}
