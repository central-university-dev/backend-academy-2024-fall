package ru.tbank
package birthday_greetings.from_func_to_trait

import scala.util.control.NoStackTrace

object domain {

  case class InvalidEmail(email: String)
      extends Exception(s"Invalid email: $email")
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
