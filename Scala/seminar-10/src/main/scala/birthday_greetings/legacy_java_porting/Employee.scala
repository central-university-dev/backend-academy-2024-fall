package ru.tbank
package birthday_greetings.legacy_java_porting

import java.time.LocalDate

case class Employee(
    firstName: String,
    lastName: String,
    birthDate: LocalDate,
    email: String
) {

  def isBirthday(today: LocalDate): Boolean =
    birthDate.getDayOfMonth == today.getDayOfMonth &&
      birthDate.getMonthValue == today.getMonthValue
}

object Employee {
  def apply(
      firstName: String,
      lastName: String,
      birthDate: String,
      email: String
  ): Employee =
    new Employee(firstName, lastName, LocalDate.parse(birthDate), email)
}
