package tbank.parallel

import cats.effect.{IO, IOApp, Ref, Resource}
import cats.effect.std.Console
import cats.syntax.parallel.*
import scala.concurrent.duration.*

import scala.io.Source

object FileFixRaceConditionExample extends IOApp.Simple {

  private def file(name: String): Resource[IO, Source] =
    Resource.fromAutoCloseable(IO.blocking(Source.fromResource(name)))

  private def incrementTotalWords(counter: Ref[IO, Int], count: Int): IO[Unit] =
    for {
      _ <- IO.sleep(0.5.seconds)
      result <- counter.updateAndGet(_ + count)
      _ <- Console[IO].println(s"Current total words count: $result")
    } yield ()

  override def run: IO[Unit] = for {
    wordCountRef <- Ref.of[IO, Int](0)
    _ <- Seq("text-1.txt", "text-2.txt", "text-3.txt")
      .parTraverse(filePath =>
        file(filePath).use { source =>
          for {
            count <- IO(source.getLines().flatMap(_.split("\\W+")).size)
            _ <- incrementTotalWords(wordCountRef, count)
            _ <- Console[IO].println(s"File: $filePath, Count: $count")
          } yield ()
        }
      )
    totalWords <- wordCountRef.get
    _ <- Console[IO].println(s"Total words count: $totalWords")
  } yield ()
}
