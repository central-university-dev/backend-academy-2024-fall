package ru.tbank
package birthday_greetings.legacy_java_porting

import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDate

class BirthdayServiceSpec extends AnyFunSuite {

  test("BirthdayService should send greetings to employees with birthday") {
    val service = new BirthdayService
    assertThrows[Throwable](
      service.sendGreetings(
        "employee_data.txt",
        LocalDate.parse("2024-10-08"),
        "localhost",
        25
      )
    )
  }

}
