package ru.tbank
package birthday_greetings.from_func_to_trait

import java.io.{BufferedReader, FileReader}
import scala.io.Source

trait Repository {
  def loadEmployees(): List[Employee]
}

object Repository {

  def buildFileRepository(fileName: String): Repository = new Repository {
    def loadEmployees(): List[Employee] =
      Source
        .fromResource(fileName)
        .getLines()
        .toList
        .flatMap { line =>
          val words = line.split(",").map(_.trim)
          for
            firstName <- words.headOption
            lastName <- words.lift(1)
            date <- words.lift(2)
            email <- words.lift(3)
            employee <- Employee
              .fromString(firstName, lastName, date, email)
              .toOption
          yield employee
        }
  }
}
