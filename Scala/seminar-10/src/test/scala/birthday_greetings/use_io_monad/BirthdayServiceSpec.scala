package ru.tbank
package birthday_greetings.use_io_monad

import birthday_greetings.use_io_monad.Repository.buildFileRepository
import birthday_greetings.use_io_monad.domain.Email
import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.EitherValues
import org.scalatest.flatspec.AsyncFlatSpec

import java.time.LocalDate

class BirthdayServiceSpec
    extends AsyncFlatSpec
    with AsyncIOSpec
    with EitherValues
    with AsyncMockFactory {

  "BirthdayService" should "send greetings to employees with birthday" in {
    val greetingsNotification = mock[GreetingsNotification]

    val testUser =
      Employee(
        "Doe",
        "John",
        LocalDate.parse("1982-10-08"),
        Email("john.doe@foobar.com").value
      )

    (greetingsNotification.sendMessage _)
      .expects(testUser)
      .returning(IO.unit)
      .once()

    buildFileRepository("employee_data.txt").use { repo =>
      BirthdayService.sendGreetings(
        repo,
        greetingsNotification,
        LocalDate.parse("2024-10-08")
      )
    }
  }

}
