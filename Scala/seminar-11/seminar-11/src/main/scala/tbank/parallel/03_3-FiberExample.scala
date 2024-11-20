package tbank.parallel

import cats.effect.{Fiber, FiberIO, IO, IOApp}
import cats.effect.std.Console

import scala.concurrent.duration.*

object FiberExample extends IOApp.Simple {

  // Функция, которая будет выполняться асинхронно
  private def longRunningTask(id: Int): IO[Int] = for {
    _ <- Console[IO].println(s"Task $id: starting...")
    _ <- IO.sleep(3.seconds)
    _ <- Console[IO].println(s"Task $id: finished!")
  } yield id

  // Основная функция приложения
  def run: IO[Unit] = for {
    // Запускаем задачу в fiber
    fiber: FiberIO[Int] <- longRunningTask(1).start
    // Запускаем вторую задачу в fiber
    fiber2: FiberIO[Int] <- longRunningTask(2).start
    // Подождем немного
    _ <- IO.sleep(1.second)
    // Отменяем первую задачу
    _ <- Console[IO].println("Cancelling task 1...") *> fiber.cancel
    // Ждем завершения второй задачи
    result <- fiber2.join // Ожидаем результата второй задачи
    _ <- Console[IO].println(s"Result of task 2: $result")
  } yield ()
}
