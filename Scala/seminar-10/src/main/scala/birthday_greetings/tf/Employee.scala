package ru.tbank
package birthday_greetings.tf

import birthday_greetings.tf
import birthday_greetings.tf.domain.Email

import java.time.LocalDate
import scala.util.Try

case class Employee(
    firstName: String,
    lastName: String,
    birthDate: LocalDate,
    email: Email
) {

  def isBirthday(today: LocalDate): Boolean =
    birthDate.getDayOfMonth == today.getDayOfMonth &&
      birthDate.getMonthValue == today.getMonthValue
}

object Employee {
  def fromString(
      firstName: String,
      lastName: String,
      birthDate: String,
      email: String
  ): Either[Throwable, Employee] =
    for {
      bd <- Try(LocalDate.parse(birthDate)).toEither
      validEmail <- Email(email)
    } yield Employee(firstName, lastName, bd, validEmail)
}
