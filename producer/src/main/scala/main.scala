
import java.util.Properties
import org.apache.kafka.clients.producer._
import ch.qos.logback.classic.{Level,Logger}
import org.slf4j.LoggerFactory

object main extends App {

    LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)

    println("Producer")

    val topic = "testtopic"

    val kafkaProps = new Properties()
    kafkaProps.put("bootstrap.servers",     "localhost:9092");
    kafkaProps.put("key.serializer",        "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProps.put("value.serializer",      "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](kafkaProps);
    for( i <- 1 to 10) {
        try {
            val record = new ProducerRecord[String, String](topic, null, s"record nr: $i");
            producer.send(record)
            println(s"----- send record: $i")
        } catch {
            case e:Throwable => println(s"----- send failed: ${e.getMessage}")
        }
    }
    producer.flush()
}
