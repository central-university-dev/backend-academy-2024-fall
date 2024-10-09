package ru.tbank

opaque type Age = Int
opaque type Email = String

trait Validator[F[_]]:
  def parseNumber(input: String): F[Long]

  def validateAge(input: Long): F[Age]

  def validateEmail(input: String): F[Email]

class OptionValidator extends Validator[Option]:
  override def parseNumber(input: String): Option[Long] = ???

  override def validateAge(input: Long): Option[Age] = ???

  override def validateEmail(input: String): Option[Email] = ???

type EitherThrowable[A] = Either[Throwable, A]

class EitherValidator extends Validator[EitherThrowable]:
  override def parseNumber(input: String): EitherThrowable[Long] = ???

  override def validateAge(input: Long): EitherThrowable[Age] = ???

  override def validateEmail(input: String): EitherThrowable[Email] = ???

case class User(age: Age, email: Email)

@main
def main(): Unit =
  val validator = EitherValidator()
  val user = for
    number <- validator.parseNumber("12345")
    age <- validator.validateAge(number)
    email <- validator.validateEmail("example@example.com")
  yield User(age, email)

  user match
    case Left(err) => println(err.getMessage)
    case Right(value) => println(value)


// попробуем абстрагироваться от конкретной реализации
def validateUser[F[_]](validator: Validator[F])(
  number: String,
  age: String,
  email: String
): F[User] =
  // не работает, потому что у нас не опредлен map, flatMap для валидатора
  for
    number <- validator.parseNumber("12345")
    age <- validator.validateAge(number)
    email <- validator.validateEmail("example@example.com")
  yield User(age, email)

// вспоминаем, что у монады есть нужные нам комбинаторы
// импортируем синтаксис для map, flatMap ("->" внутри for-comprehension)
import cats.Monad
import cats.syntax.flatMap._ 
import cats.syntax.functor._

// и добавляем context bound на F -> "F[_]: Monad"
def validateUser2[F[_]: Monad](validator: Validator[F])(
  number: String,
  age: String,
  email: String
): F[User] =
  for
    number <- validator.parseNumber("12345")
    age <- validator.validateAge(number)
    email <- validator.validateEmail("example@example.com")
  yield User(age, email)

// теперь эта функция будет работать для абсолютно любой реализации Validator,
// где F является монадой
