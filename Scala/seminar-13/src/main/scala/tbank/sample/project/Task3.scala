package tbank.sample.project

import cats.Parallel
import cats.data.{OptionT, ReaderT}
import cats.effect.*
import cats.effect.std.Console
import cats.syntax.applicative.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.parallel.*

import java.util.UUID

object Task3 extends IOApp.Simple {
  case class FindForActor(name: String, baconName: String)

  case class Actor(id: UUID, name: String, movies: List[UUID])
  case class Movie(id: UUID, name: String, actors: List[UUID])

  val moviesData: Map[UUID, Movie] = Map(
    UUID.fromString("f1b5de4f-c0a8-4e39-bc3d-9230eac622c7") -> Movie(
      UUID.fromString("f1b5de4f-c0a8-4e39-bc3d-9230eac622c7"),
      "Movie A",
      List(
        UUID.fromString("4c78e17e-e4b1-4c9f-81fd-d6b2351025a5")
      )
    ),
    UUID.fromString("0a4d31be-e59f-46af-890c-b2bc3ba371b0") -> Movie(
      UUID.fromString("0a4d31be-e59f-46af-890c-b2bc3ba371b0"),
      "Movie B",
      List(
        UUID.fromString("4c78e17e-e4b1-4c9f-81fd-d6b2351025a5"),
        UUID.fromString("c1dac81c-29e4-459c-acb3-d9669648c8c1"),
        UUID.fromString("aef15cf2-13f6-41c3-bb73-0f9c99da8c54")
      )
    )
  )

  val actorsData: Map[UUID, Actor] = Map(
    UUID.fromString("4c78e17e-e4b1-4c9f-81fd-d6b2351025a5") -> Actor(
      UUID.fromString("4c78e17e-e4b1-4c9f-81fd-d6b2351025a5"),
      "Ryan Gosling",
      List(UUID.fromString("f1b5de4f-c0a8-4e39-bc3d-9230eac622c7")) // Movie A
    ),
    UUID.fromString("aef15cf2-13f6-41c3-bb73-0f9c99da8c54") -> Actor(
      UUID.fromString("aef15cf2-13f6-41c3-bb73-0f9c99da8c54"),
      "Actor 1",
      List(UUID.fromString("0a4d31be-e59f-46af-890c-b2bc3ba371b0")) // Movie B
    ),
    UUID.fromString("c1dac81c-29e4-459c-acb3-d9669648c8c1") -> Actor(
      UUID.fromString("c1dac81c-29e4-459c-acb3-d9669648c8c1"),
      "Actor 2",
      List(UUID.fromString("0a4d31be-e59f-46af-890c-b2bc3ba371b0")) // Movie B
    ),
    UUID.fromString("f5b4f2c0-e8a2-4d70-9738-e36a209fbd17") -> Actor(
      UUID.fromString("f5b4f2c0-e8a2-4d70-9738-e36a209fbd17"),
      "Kevin Bacon",
      List(UUID.fromString("0a4d31be-e59f-46af-890c-b2bc3ba371b0")) // Movie B
    )
  )

  def getMovieById[F[_]: Concurrent: Console](
    movieId: UUID
  ): OptionT[F, Movie] =
    OptionT {
      for {
        _          <- Console[F].println(s"getMovieById: $movieId")
        movieRef   <- Ref.of[F, Map[UUID, Movie]](moviesData)
        maybeMovie <- movieRef.get.map(_.get(movieId))
        _          <- Console[F].println(s"getMovieById: $maybeMovie")
      } yield maybeMovie
    }

  def getActorByName[F[_]: Concurrent: Console](
    name: String
  ): OptionT[F, Actor] =
    OptionT {
      for {
        _        <- Console[F].println(s"Searching for actor $name")
        actorRef <- Ref.of[F, Map[UUID, Actor]](actorsData)
        maybeActor <-
          actorRef.get.map(_.values.find(_.name == name))
        _ <- maybeActor.fold(Console[F].println(s"Actor $name not found"))(_ =>
               Console[F].println(s"Actor $name found")
             )
      } yield maybeActor
    }

  def getActorById[F[_]: Concurrent: Console](
    actorId: UUID
  ): OptionT[F, Actor] =
    OptionT {
      for {
        _          <- Console[F].println(s"getActorById: $actorId")
        actorRef   <- Ref.of[F, Map[UUID, Actor]](actorsData)
        maybeActor <- actorRef.get.map(_.get(actorId))
        _          <- Console[F].println(s"getActorById: $maybeActor")
      } yield maybeActor
    }

  def getBaconNumber[F[_]: Concurrent: Parallel: Console](
    actorId: UUID,
    baconId: UUID
  ): F[Int] = ???

  def app[F[_]: Concurrent: Parallel: Console](
    searchParams: FindForActor
  ): F[Int] =
    (for {
      baconId     <- getActorByName[F](searchParams.baconName)
      toFindName  <- getActorByName[F](searchParams.name)
      baconNumber <- OptionT.liftF(getBaconNumber[F](toFindName.id, baconId.id))
    } yield baconNumber).getOrElse(-1)

  override def run: IO[Unit] = (for {
    searchParams <- ReaderT.ask[IO, FindForActor]
    number       <- ReaderT.liftF(app[IO](searchParams))
    _ <-
      ReaderT.liftF(
        Console[IO].println(
          s"The distance between ${searchParams.name} and ${searchParams.baconName} is $number"
        )
      )
  } yield number)
    .run(FindForActor("Ryan Gosling", "Kevin Bacon"))
    .void
}
