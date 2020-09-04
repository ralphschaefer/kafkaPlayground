package dtos

import com.sksamuel.avro4s.AvroSchema
import org.apache.avro.Schema

case class Message(item1: Int, item2: String, h: hh)
case class hh(a:String, b:Boolean)

object Message {
  val schema: Schema = AvroSchema[Message]
}
