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

import scala.concurrent.ExecutionContext.global


object Messages {
  case class Result(msg: String)
  case class Time(time:String, uuid:String)
  case class AvroWrapper(avro: String)
  case class Echo(msg: String)
  implicit val echoDecoder = jsonOf[IO, Echo]
  implicit val avroWrapperDecoder = jsonOf[IO, AvroWrapper]
}


// todo Service(POST): Display avro item
// todo Service(GET): Generate localTime with UUID

object Main extends IOApp {

  import Messages._

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "echo" / name =>
      for {
        resp <- Ok(Result(name).asJson)
      } yield resp
    case req @ POST -> Root / "echo" =>
      for {
        echo <- req.as[Echo]
        resp <- Ok(Result(echo.msg).asJson)
      } yield resp
    case GET -> Root / "test" =>
      Ok("test")
  }.orNotFound

  def run(args: List[String]): IO[ExitCode] = BlazeServerBuilder[IO](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

