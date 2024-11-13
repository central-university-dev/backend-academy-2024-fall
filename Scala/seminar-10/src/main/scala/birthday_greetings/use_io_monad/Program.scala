package ru.tbank
package birthday_greetings.use_io_monad

import birthday_greetings.use_io_monad.GreetingsNotification.buildSmtpGreetingsNotification
import birthday_greetings.use_io_monad.Repository.buildFileRepository
import cats.effect.{IO, IOApp}

import java.time.LocalDate

object Program extends IOApp.Simple {

  override def run: IO[Unit] =
    buildFileRepository("employee_data.txt").use { repo =>

      val greetingsNotification =
        buildSmtpGreetingsNotification("localhost", 25)

      BirthdayService
        .sendGreetings(repo, greetingsNotification, LocalDate.now())
        .handleErrorWith(err =>
          IO.println(s"Unexpected error: ${err.getMessage}")
        )
    }
}
