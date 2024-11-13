package ru.tbank
package birthday_greetings.tf

import cats.Monad
import cats.syntax.all.*
import cats.syntax.traverse.*

import java.time.LocalDate

object BirthdayService {

  def sendGreetings[F[_]: Monad](
      repository: Repository[F],
      notification: GreetingsNotification[F],
      today: LocalDate
  ): F[Unit] =
    for {
      employees <- repository.loadEmployees()
      birthdayEmployees = employees.filter(_.isBirthday(today))
      _ <- birthdayEmployees.traverse(notification.sendMessage)
    } yield ()
}
