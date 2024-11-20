package tbank.parallel

import cats.effect.{IO, IOApp, Resource}
import cats.effect.std.Console
import cats.syntax.parallel.*

import scala.io.Source

object FileRaceConditionExample extends IOApp.Simple {

  var totalWords: Int = 0

  private def file(name: String): Resource[IO, Source] =
    Resource.make(IO.blocking(Source.fromResource(name)))(file =>
      IO(file.close())
    )

  private def incrementTotalWords(count: Int): IO[Unit] = IO {
    val currentCount = totalWords
    // Имитация задержки, чтобы ухудшить условия гонки
    Thread.sleep(50)
    totalWords = currentCount + count
    println(s"Current total words count: $totalWords")
  }

  override def run: IO[Unit] = Seq("text-1.txt", "text-2.txt", "text-3.txt")
    .parTraverse(filePath =>
      file(filePath).use { source =>
        for {
          count <- IO(source.getLines().flatMap(_.split("\\W+")).size)
          _ <- incrementTotalWords(count)
          _ <- Console[IO].println(s"File: $filePath, Count: $count")
        } yield ()
      }
    )
    .flatTap(_ => Console[IO].println(s"Total words count: $totalWords"))
    .void
}
