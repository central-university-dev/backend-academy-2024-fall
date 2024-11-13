package ru.tbank
package birthday_greetings.from_func_to_trait

import birthday_greetings.from_func_to_trait.Repository.buildFileRepository
import birthday_greetings.from_func_to_trait.domain.Email
import org.scalamock.scalatest.MockFactory
import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDate

class BirthdayServiceSpec
    extends AnyFunSuite
    with MockFactory
    with EitherValues {

  test("BirthdayService should send greetings to employees with birthday") {
    val repository = buildFileRepository("employee_data.txt")
    val greetingsNotification = mock[GreetingsNotification]

    val testUser =
      Employee(
        "Doe",
        "John",
        LocalDate.parse("1982-10-08"),
        Email("john.doe@foobar.com").value
      )

    (greetingsNotification.sendMessage _).expects(testUser).returning(()).once()

    BirthdayService.sendGreetings(
      repository,
      greetingsNotification,
      LocalDate.parse("2024-10-08")
    )
  }

}
