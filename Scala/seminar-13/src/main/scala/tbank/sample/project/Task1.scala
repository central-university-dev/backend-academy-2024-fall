package tbank.sample.project

import cats.effect.*
import cats.effect.std.{Console, Random}
import cats.syntax.all.*

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt
import scala.util.matching.Regex

/*
Наивная реализация паттерна producer-consumer: producer-consumer.png

Паттерн "Producer-Consumer" (производитель-потребитель) — это классический шаблон проектирования, используемый для
организации взаимодействия между процессами или потоками в многопоточных системах. Он применяется для отделения
этапов производства данных от их обработки, что позволяет эффективно управлять потоками данных и использовать
асинхронность. Вот ключевые компоненты и принципы этого паттерна:

Основные Компоненты:
- Производитель (Producer):
  Отвечает за создание данных или их извлечение из внешних источников. В многопоточном контексте это
   может быть поток, который производит данные и помещает их в общую очередь или буфер.
- Потребитель (Consumer):
  Берет данные из очереди или буфера и обрабатывает их. Это может быть один или несколько потоков, в
   зависимости от необходимости нагрузки.
- Буфер/Очередь (Buffer/Queue):
  Хранит данные, передаваемые от производителя к потребителю. Это может быть неблокирующая очередь или блокирующая
   структура данных, обеспечивающая потокобезопасность и доступ к данным.

Такой подход часто применяется в системах обработки данных, где необходимо обрабатывать потоки событий
(например, журналы активности, потоки данных IoT). Так же в приложениях с многопоточной обработкой, чтобы
разграничить операции ввода-вывода и вычисления. И в микросервисной архитектуре, где сервисы обмениваются
сообщениями через посредника (например, через очередь сообщений).

Задание:

Разработать небольшую систему мониторинга логов, в которой одна или несколько услуг (producers) генерируют записи
логов, а другой компонент (consumer) обрабатывает и анализирует эти логи в режиме реального времени.

- Производитель случайно генерирует сообщение с уровнем логгирования
- Потребитель читает данное сообщение и ведет подсчет количества различных уровней
 */
object Task1 extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      random    <- Random.scalaUtilRandom[IO]
      queueR    <- Ref.of[IO, Queue[String]](Queue.empty[String])
      countersR <- Ref.of[IO, (Int, Int, Int, Int)]((0, 0, 0, 0))
      res <-
        (consumer(queueR, countersR), producer(queueR, random))
          .parFlatMapN((_, _) =>
            countersR.get.flatMap { (info, warn, error, without) =>
              Console[IO].println(
                s"Found Info: $info, Warn: $warn, Error: $error, Without: $without"
              )
            }.as(ExitCode.Success)
          ) // Run producer and consumer in parallel until done (likely by user cancelling with CTRL-C)
          .handleErrorWith { t =>
            Console[IO].errorln(s"Error caught: ${t.getMessage}")
              .as(ExitCode.Error)
          }
    } yield res

  private def producer[F[_]: Temporal: Console](
    queueR: Ref[F, Queue[String]],
    random: Random[F]
  ): F[Unit] =
    for {
      message <- generateMessage[F](random)
      _       <- queueR.getAndUpdate(_.enqueue(message))
      _       <- producer(queueR, random)
    } yield ()

  private def generateMessage[F[_]: Temporal: Console](
    random: Random[F]
  ): F[String] =
    random.nextDouble.map {
      case d if d < 0.25 => "[Info] Information log message"
      case d if d < 0.5  => "[Warn] Warning!!!"
      case d if d < 0.75 => "[Error] Something went wrong"
      case _             => "Without level"
    }.flatMap { message =>
      Console[F].println(s"Generated message: $message") >>
      Temporal[F].sleep(1.second) as
      message
    }

  private def consumer[F[_]: Temporal: Console](
    queueR: Ref[F, Queue[String]],
    countersR: Ref[F, (Int, Int, Int, Int)]
  ): F[Unit] =
    for {
      messageOpt <- queueR.modify { queue =>
                      queue.dequeueOption.fold((queue, Option.empty[String])) {
                        case (m, queue) => (queue, Some(m))
                      }
                    }
      _ <- handleMessage(countersR, messageOpt)
      _ <- consumer(queueR, countersR)
    } yield ()

  private def handleMessage[F[_]: Temporal: Console](
    countersR: Ref[F, (Int, Int, Int, Int)],
    messageOpt: Option[String]
  ): F[Unit] = countersR.flatModify { (info, warn, error, without) =>
    messageOpt match {
      case Some(message) =>
        message match {
          case LevelRegex("Info", message) =>
            (
              (info + 1, warn, error, without),
              Console[F].println(s"Info message found: $message")
            )
          case LevelRegex("Warn", message) =>
            (
              (info, warn + 1, error, without),
              Console[F].println(s"Warn message found: $message")
            )
          case LevelRegex("Error", message) =>
            (
              (info, warn, error + 1, without),
              Console[F].println(s"Error message found: $message")
            )
          case _ =>
            (
              (info, warn, error, without + 1),
              Console[F].println(s"Message without level found: $message")
            )
        }
      case None =>
        (
          (info, warn, error, without),
          Console[F].println("New messages not found")
        )
    }
  } >> Temporal[F].sleep(400.millis)

  private val LevelRegex: Regex = """\[(Info|Warn|Error)] (.+)""".r
}
