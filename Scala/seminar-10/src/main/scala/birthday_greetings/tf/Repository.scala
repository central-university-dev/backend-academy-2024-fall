package ru.tbank
package birthday_greetings.tf

import birthday_greetings.use_io_monad.domain.InvalidInput
import cats.effect
import cats.effect.{Async, Resource}
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.traverse.*

import scala.io.{BufferedSource, Source}

trait Repository[F[_]] {
  def loadEmployees(): F[List[Employee]]
}

class FileRepository[F[_]: MonadCancelThrow](s: BufferedSource)
    extends Repository[F] {
  def loadEmployees(): F[List[Employee]] = {
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
    MonadCancelThrow[F].fromEither(employeesE)
  }
}

object Repository {

  def buildFileRepository[F[_]: Async](
      fileName: String
  ): Resource[F, Repository[F]] =
    Resource
      .fromAutoCloseable(Async[F].delay(Source.fromResource(fileName)))
      .map(source => new FileRepository(source))
}
