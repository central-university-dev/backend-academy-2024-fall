package ru.tbank
package birthday_greetings.tf

import birthday_greetings.tf.GreetingsNotification.buildSmtpGreetingsNotification
import birthday_greetings.tf.Repository.buildFileRepository
import cats.effect.{IO, IOApp}

import java.time.LocalDate

object Program extends IOApp.Simple {

  override def run: IO[Unit] =
    buildFileRepository[IO]("employee_data.txt").use { repo =>

      val greetingsNotification =
        buildSmtpGreetingsNotification[IO]("smtp://localhost", 2525, "", "")

      BirthdayService
        .sendGreetings(repo, greetingsNotification, LocalDate.now())
        .handleErrorWith(err =>
          IO.println(s"Unexpected error: ${err.getMessage}")
        )
    }
}
