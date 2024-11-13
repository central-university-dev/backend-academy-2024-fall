package ru.tbank
package birthday_greetings.use_io_monad

import scala.util.control.NoStackTrace

object domain {

  case class InvalidEmail(email: String)
      extends Exception(s"Invalid email: $email")
      with NoStackTrace

  case class InvalidInput(input: String)
      extends Exception(s"Invalid employee data: $input")
      with NoStackTrace

  opaque type Email = String

  object Email {

    def apply(email: String): Either[InvalidEmail, Email] = Either.cond(
      email.contains("@"),
      email,
      InvalidEmail(email)
    )

    extension (e: Email) {
      def value: String = e
    }
  }
}
