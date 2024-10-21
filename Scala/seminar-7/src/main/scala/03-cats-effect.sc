// В cats-effect уже есть IO-монада

import cats.effect.IO

import scala.util.Random
import scala.util.control.NoStackTrace

// Можно поднять любое значение до IO

IO.pure(42)

// Сделать синхронные вычисления с side эффектами

def effectfulComputation = ???

val programIO = IO(effectfulComputation)

// Это аналог impureCompute()
// Чтобы его выполнить, нужно передать IORuntime, то есть способ выполнения вычислений
// Подробнее о том, что это, мы обсудим в следующих семинарах
// На практике IORuntime предоставляется библиотекой cats-effect
// В worksheet (не в тестах, не в main) можно использовать глобальный рантайм
import cats.effect.unsafe.implicits.global
programIO.unsafeRunSync()

// Обработка ошибок

def error: Exception =
  new RuntimeException("Something went wrong") with NoStackTrace

// Рассмотрим программу, которая кидает ошибку

def program: IO[Unit] =
  for
    _ <- IO.println("Hello")
    _ = throw error // это классическое исключение из Java
    _ <- IO.println("World")
  yield ()

program.unsafeRunSync()

// На scala не принято бросать исключения, поскольку это тоже побочный эффект, который лишает функцию ссылочной прозрачности

// возвращает "значение", указывающее на то, что произошло исключение,
// вместо того, чтобы бросать его
IO.raiseError(error)

def program2: IO[Unit] =
  for
    _ <- IO.println("Hello")
    _ <- IO.raiseError(error)
    _ <- IO.println("World")
  yield ()

program2.unsafeRunSync()
// Вопрос: будет ли напечатало "Hello" или "World"?

// можем обработать ошибку различными способами
program2
  .onError(e => IO.println(s"Got error: ${e.getMessage}"))
  .handleErrorWith(_ => IO.unit)
  .handleError(_ => ())
  .recoverWith { case _: RuntimeException => IO.unit }
  .recover { case _: RuntimeException => () }
  .redeemWith(error => IO.unit, value => IO.unit)
  .redeem(error => (), value => ())
  .unsafeRunSync()

// Также можно добывать ошибки из уже знакомых нам классов

import scala.util.Try

IO.fromTry(Try(42))
IO.fromOption(Some(1))(error)
IO.fromEither(Left[Throwable, Unit](error))

// В обратную сторону можно получить Either из IO

val mayBeError = IO(1 / Random.nextInt(2))

mayBeError.attempt.unsafeRunSync() match
  case Left(err)    => println(s"Got error: ${err.getMessage}")
  case Right(value) => println(s"Got value: $value")

IO.raiseError(error).option.unsafeRunSync()

IO(Left[Throwable, Unit](error)).rethrow

// Работа с ресурсами

// Для безопасной работы с ресурсами существует cats.effect.Resource

import cats.effect.Resource
import java.io.FileInputStream

val stream = Resource.fromAutoCloseable(IO(new FileInputStream("file.txt")))

stream.use(inputStream => ???)

// Большинство библиотек уже предоставляет ресурсы, обернутые в cats.effect.Resource. Например, http-клиенты, клиенты к базам и т.д.
// Если дефолтной реализации нет, можно создать закрыть ресурс самостоятельно

Resource.make(
  acquire = IO(new FileInputStream("file.txt"))
)(
  release = file => IO(file.close())
)
