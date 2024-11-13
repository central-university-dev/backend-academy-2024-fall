package ru.tbank
package birthday_greetings.from_func_to_trait

import birthday_greetings.from_func_to_trait.GreetingsNotification.buildSmtpGreetingsNotification
import birthday_greetings.from_func_to_trait.Repository.buildFileRepository

import java.time.LocalDate

object Program {
  def main(args: Array[String]): Unit = {

    val repository = buildFileRepository("employee_data.txt")
    val greetingsNotification = buildSmtpGreetingsNotification("localhost", 25)

    BirthdayService.sendGreetings(
      repository,
      greetingsNotification,
      LocalDate.now()
    )
  }
}
