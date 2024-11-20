package tbank.parallel

import cats.effect.std.{Semaphore, Console}
import cats.syntax.parallel.*
import cats.effect.{IO, IOApp}

import scala.concurrent.duration.*

object SemaphoreExample extends IOApp.Simple {
  private def semaphoreExample: IO[Unit] = for {
    // Создаём семафор, позволяющий двум потокам одновременно получить доступ к ресурсу
    semaphore <- Semaphore[IO](2) // 2 - это максимальное количество одновременно выполняемых задач

    // Определяем задачу, которая будет занимать семафор, выполнять некоторую работу и освобождать семафор
    task = (id: Int) => {
      // Начинаем задачу
      for {
        _ <- Console[IO].println(s"Task $id: waiting for the semaphore...")
        _ <- semaphore.acquire // Получение доступа к ресурсу
        _ <- Console[IO].println(s"Task $id: acquired the semaphore!")
        _ <- IO.sleep(2.seconds) // Имитация длительной работы
        _ <- Console[IO].println(s"Task $id: releasing the semaphore...")
        _ <- semaphore.release // Освобождаем семафор
      } yield ()
    }

    // Запуск нескольких задач параллельно
    tasks = List.tabulate(5)(task)
    _ <- tasks.parTraverse(identity) // Запускаем все задачи параллельно
  } yield ()

  override def run: IO[Unit] = semaphoreExample
}
