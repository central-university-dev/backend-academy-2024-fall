package tbank.cats.effect.task

import cats.{Functor, Monad}
import cats.data.ReaderT
import cats.syntax.either._

final case class MEitherT[F[_], E, V](value: F[Either[E, V]]) extends AnyVal {
  def fold[VV](fa: E => VV, fb: V => VV)(implicit F: Functor[F]): F[VV] = ???
  def recoverWith(pf: PartialFunction[E, MEitherT[F, E, V]])(implicit
    F: Monad[F]
  ): MEitherT[F, E, V] = ???
  def bimap[EE, VV](fa: E => EE, fb: V => VV)(implicit
    F: Functor[F]
  ): MEitherT[F, EE, VV] = ???
  def flatMap[EE >: E, VV](f: V => MEitherT[F, EE, VV])(implicit
    F: Monad[F]
  ): MEitherT[F, EE, VV] = ???
  def map[VV](f: V => VV)(implicit
    F: Functor[F]
  ): MEitherT[F, E, VV] = ???
}

object MEitherT {
  def pure[F[_], E, V](v: V)(implicit F: Monad[F]): MEitherT[F, E, V] = ???
  def failed[F[_], E, V](e: E)(implicit F: Monad[F]): MEitherT[F, E, V] = ???

  // Мб тут еще реализовать какой-то инстанс?
}

/*
Задание:
- читать данные пользователя из консоли
- если введены не латиницей, то кидать исключение через MEitherT и повторять чтение
- если ввод был неверный 3 раза, то взять имя и фамилию по умолчанию из конфига, который в ReaderT
 */

import cats.effect.{IO, IOApp}

object Task extends IOApp.Simple {
  private type EitherThrow[A] = MEitherT[IO, Throwable, A]

  private case class User(firstName: String, lastName: String)

  private case class Config(
    defaultFirstName: String = "None",
    defaultLastName: String = "None"
  )

  private def program: ReaderT[EitherThrow, Config, User] = ???
  override def run: IO[Unit] = program.run(Config()).fold(
    th => println(s"Something went wrong: $th"),
    user => println(s"Successfully read user: $user")
  )
}
