package my.simpleREST


import cats.data.Kleisli
import cats.effect._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.server.blaze._
import com.sksamuel.avro4s.AvroSchema
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext.global


object Messages extends StrictLogging {
  trait loggable
  case class Result(msg: String)
  case class Time(time:String, uuid:String)
  // case class AvroWrapper(avro: String)
  case class Echo(msg: String) extends loggable
  case class SchemaResponse(name:String, schema: String) extends loggable
  implicit val echoDecoder = jsonOf[IO, Echo]
  implicit val schemaDecoder = jsonOf[IO, SchemaResponse]
  // implicit val avroWrapperDecoder = jsonOf[IO, AvroWrapper]

  def log(item: loggable): IO[Unit] = IO {
    logger.info(item.toString)
  }

  def log(item: String): IO[Unit] = IO {
    logger.info(item)
  }

}


// todo Service(POST): Display avro item
// todo Service(GET): Generate localTime with UUID

object Main extends IOApp with StrictLogging {

  import Messages._

  case class Error(msg:String) extends Exception(msg)

  def evalSchema(schemaName:String): IO[String] = IO{
    schemaName match {
      case "echo" => AvroSchema[Messages.Echo].toString(false)
      case _ => throw Error(s"schema '$schemaName' not found'")
    }
  }

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "echo" / name =>
      logger.debug("get echo")
      for {
        resp <- Ok(Result(name).asJson)
      } yield resp
    case req @ POST -> Root / "echo" =>
      logger.debug("post echo")
      for {
        _ <- log(req.toString())
        str <- req.as[String]
        _ <- log(str)
        echo <- req.as[Echo]
        _ <- log(echo)
        resp <- Ok(Result(echo.msg).asJson)
      } yield resp
    case GET -> Root / "schema" / name =>
      logger.debug("schema {}", name)
      for {
        s <- evalSchema(name)
        _ <- log(SchemaResponse(name, s))
        resp <- Ok(SchemaResponse(name, s).asJson)
      } yield(resp)
    case GET -> Root / "test" =>
      logger.debug("test")
      Ok("test")
  }.orNotFound

  logger.info("Startup Server")
  def run(args: List[String]): IO[ExitCode] = BlazeServerBuilder[IO](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

