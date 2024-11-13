package ru.tbank
package birthday_greetings.use_io_monad

import cats.effect.IO
import cats.syntax.traverse.*

import java.time.LocalDate

object BirthdayService {

  def sendGreetings(
      repository: Repository,
      notification: GreetingsNotification,
      today: LocalDate
  ): IO[Unit] =
    for {
      employees <- repository.loadEmployees()
      birthdayEmployees = employees.filter(_.isBirthday(today))
      _ <- birthdayEmployees.traverse(notification.sendMessage)
    } yield ()
}
