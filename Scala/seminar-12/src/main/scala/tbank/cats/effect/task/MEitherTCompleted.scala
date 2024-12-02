package tbank.cats.effect.task

import cats.{Functor, Monad}
import cats.effect.{IO, IOApp}
import cats.data.ReaderT
import cats.effect.std.Console
import cats.syntax.either._

object MEitherTCompleted extends IOApp.Simple {
  final case class MEitherT[F[_], E, V](value: F[Either[E, V]]) extends AnyVal {
    def fold[VV](fa: E => VV, fb: V => VV)(implicit F: Functor[F]): F[VV] =
      F.map(value) {
        case Left(a) => fa(a)
        case Right(b) => fb(b)
      }

    def recoverWith(pf: PartialFunction[E, MEitherT[F, E, V]])(implicit
                                                               F: Monad[F]
    ): MEitherT[F, E, V] = MEitherT(F.flatMap(value) {
      case Left(a) if pf.isDefinedAt(a) => pf(a).value
      case other => F.pure(other)
    })

    def bimap[EE, VV](fa: E => EE, fb: V => VV)(implicit
                                                F: Functor[F]
    ): MEitherT[F, EE, VV] = MEitherT(F.map(value) {
      case Left(a) => Left(fa(a))
      case Right(e) => Right(fb(e))
    })

    def flatMap[EE >: E, VV](f: V => MEitherT[F, EE, VV])(implicit
                                                          F: Monad[F]
    ): MEitherT[F, EE, VV] = MEitherT(F.flatMap(value) {
      case Left(a) => F.pure(Left(a))
      case Right(e) => f(e).value
    })

    def map[VV](f: V => VV)(implicit
                            F: Functor[F]
    ): MEitherT[F, E, VV] = MEitherT(F.map(value) {
      case Left(a) => Left(a)
      case Right(e) => Right(f(e))
    })
  }

  object MEitherT {
    def pure[F[_], E, V](v: V)(implicit F: Monad[F]): MEitherT[F, E, V] =
      MEitherT(F.pure(v.asRight[E]))

    def failed[F[_], E, V](e: E)(implicit F: Monad[F]): MEitherT[F, E, V] =
      MEitherT(F.pure(e.asLeft[V]))
  }
  
  private type EitherThrow[A] = MEitherT[IO, Throwable, A]

  private case class User(firstName: String, lastName: String)

  private case class Config(
                             defaultFirstName: String = "None",
                             defaultLastName: String = "None"
                           )

  private def readLine(fieldName: String): IO[String] = for {
    _     <- Console[IO].println(s"Enter $fieldName:")
    input <- Console[IO].readLine
  } yield input

  private def getUser: EitherThrow[User] = for {
    firstName <-
      MEitherT(
        readLine("first name").map(l =>
          Either.cond(l.forall(_.isLetter), l, new Throwable("Not latin"))
        )
      )
    lastName <-
      MEitherT(
        readLine("last name").map(l =>
          Either.cond(l.forall(_.isLetter), l, new Throwable("Not latin"))
        )
      )
  } yield User(firstName, lastName)

  private def program: ReaderT[EitherThrow, Config, User] = ReaderT { config =>
    def loop(retries: Int): EitherThrow[User] =
      if (retries > 2)
        MEitherT.pure(User(config.defaultFirstName, config.defaultLastName))
      else getUser.recoverWith(_ => loop(retries + 1))

    loop(0)
  }

  override def run: IO[Unit] = program.run(Config()).fold(
    th => println(s"Something went wrong: $th"),
    user => println(s"Successfully read user: $user")
  )
}
