package ru.tbank
package birthday_greetings

import birthday_greetings.tf.Employee

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDate

class EmployeeTest extends AnyFunSuite with EitherValues {

  test("birthday") {
    val employee = Employee(
      "foo",
      "bar",
      LocalDate.parse("2000-11-27"),
      tf.domain.Email("a@b.c").value
    )
    assert(
      !employee.isBirthday(LocalDate.parse("2024-11-11")),
      "not his birthday"
    )
    assert(employee.isBirthday(LocalDate.parse("2024-11-27")), "his birthday")
  }

}
