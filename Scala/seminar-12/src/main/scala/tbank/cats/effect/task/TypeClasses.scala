package tbank.cats.effect.task

import cats.effect.{IO, IOApp}

import java.time.Instant
import scala.util.control.NonFatal

object TypeClasses extends IOApp.Simple {
  private case class User(
    id: String,
    createdAt: Instant,
    firstName: String,
    lastName: String
  )

  /*
  Задание:
  - Прочитать данные пользователя: имя/фамилию // Concole
  - если возникла ошибка, сообщить о ней пользователю и попробовать ввод снова // MonadCancel
  - если все прошло успешно, то сгенерировать id пользователя, как набор alphanumeric // Unique
  - получить время создания пользователя // Clock
  - в качестве сохранения в БД - записать в файл данные пользователя, в csv формате (блокирующее действие) // MonadCancel
  - на сохранение установить таймаут через отмену на 5 секунд и поиграть с Temporal.sleep
   */
  private def register[F[_]]: F[User] = ???

  override def run: IO[Unit] = register[IO]
    .flatMap(IO.println)
    .recoverWith { case NonFatal(e) => IO.println(e) }

}
