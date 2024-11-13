package ru.tbank
package birthday_greetings.tf

import cats.effect.Sync
import cats.syntax.functor.*
import emil.{MailConfig, SSLType}
import emil.builder.{From, MailBuilder, Subject, TextBody, To}
import emil.javamail.JavaMailEmil

trait GreetingsNotification[F[_]] {
  def sendMessage(e: Employee): F[Unit]
}

object GreetingsNotification {

  def buildSmtpGreetingsNotification[F[_]: Sync](
      smtpHost: String,
      smtpPort: Int,
      user: String,
      pass: String
  ): GreetingsNotification[F] = {
    val conf =
      MailConfig(s"$smtpHost:$smtpPort", user, pass, SSLType.NoEncryption)
    val emil = JavaMailEmil[F]()

    new GreetingsNotification[F] {
      def sendMessage(employee: Employee): F[Unit] = {
        val mail = MailBuilder.build[F](
          From("sender@here.com"),
          To(employee.email.value),
          Subject("Happy Birthday!"),
          TextBody(s"Happy Birthday, dear ${employee.firstName}!")
        )

        emil(conf).send(mail).void
      }
    }
  }

}
