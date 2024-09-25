package tbank.implicits

import java.time.LocalDate

object ExceptionsTask {
  import Validator._
  
  @main
  def main(): Unit = 
    println(validateUser(User("Ivan", "Petrov", LocalDate.parse("2001-12-03"))))
}

object Validator {
  case class User(firstName: String, lastName: String, birthDate: LocalDate)

  opaque type ValidUser = User

  def validateUser(user: User): ValidUser = ???

  def validateUserChecked(user: User): Either[Throwable, ValidUser] = ???

  // After implicit task
  def validateNotFallFirst(user: User):  Either[List[Throwable], ValidUser] = ???
}
