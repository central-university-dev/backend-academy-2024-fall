package ru.tbank
package birthday_greetings.use_io_monad

import birthday_greetings.use_io_monad.domain.InvalidInput
import cats.effect.{IO, Resource}
import cats.syntax.traverse.*

import scala.io.{BufferedSource, Source}

trait Repository {
  def loadEmployees(): IO[List[Employee]]
}

class FileRepository(s: BufferedSource) extends Repository {
  def loadEmployees(): IO[List[Employee]] = {
    val employeesE = s.getLines().toList.traverse { line =>
      val words = line.split(",").map(_.trim)
      val employeeData =
        for
          firstName <- words.headOption
          lastName <- words.lift(1)
          date <- words.lift(2)
          email <- words.lift(3)
        yield (firstName, lastName, date, email)

      for
        y <- employeeData.toRight(InvalidInput(line))
        (firstName, lastName, date, email) = y
        employee <- Employee.fromString(firstName, lastName, date, email)
      yield employee
    }
    IO.fromEither(employeesE)
  }
}

object Repository {

  def buildFileRepository(fileName: String): Resource[IO, Repository] =
    Resource
      .fromAutoCloseable(IO(Source.fromResource(fileName)))
      .map(source => new FileRepository(source))
}
