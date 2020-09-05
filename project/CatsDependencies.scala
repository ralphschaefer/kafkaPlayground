import sbt._

object CatsDependencies {
  lazy val catsVersion = "2.1.1"
  lazy val catsEffectVersion = "2.1.4"
  lazy val http4sVersion = "0.21.7"
  lazy val libs = Seq(
    "org.typelevel" %% "cats-core" % catsVersion withSources() withJavadoc(),
    "org.typelevel" %% "cats-effect" % catsEffectVersion withSources() withJavadoc(),
    "org.http4s" %% "http4s-dsl" % http4sVersion withSources() withJavadoc(),
    "org.http4s" %% "http4s-blaze-server" % http4sVersion withSources() withJavadoc(),
    "org.http4s" %% "http4s-blaze-client" % http4sVersion withSources() withJavadoc(),
    "org.http4s" %% "http4s-circe" % http4sVersion withSources() withJavadoc(),
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-literal" % "0.13.0"
  )
}

