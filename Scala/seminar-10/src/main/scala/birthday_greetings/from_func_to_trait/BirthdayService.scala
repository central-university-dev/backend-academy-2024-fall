package ru.tbank
package birthday_greetings.from_func_to_trait

import java.time.LocalDate

object BirthdayService {

  def sendGreetings(
      repository: Repository,
      notification: GreetingsNotification,
      today: LocalDate
  ): Unit =
    repository
      .loadEmployees()
      .filter(_.isBirthday(today))
      .foreach(e => notification.sendMessage(e))
}
