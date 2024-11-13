package ru.tbank
package birthday_greetings.legacy_java_porting

import java.time.LocalDate

object Program {
  def main(args: Array[String]): Unit = {
    val service = new BirthdayService
    service.sendGreetings("employee_data.txt", LocalDate.now(), "localhost", 25)
  }
}
