/*
`cats-effect` предоставляет иерархию тайпклассов для работы с конкурентными вычислениями

Показать картинку (hierarchy-impure.jpeg)

### Monad Cancel

Тайпкласс для работы с отменой:
 */

import cats.{Applicative, MonadError}
import cats.effect.{kernel, Outcome, Poll}
import cats.effect.unsafe.implicits.global

trait MonadCancel[F[_], E] extends MonadError[F, E] {

  def canceled: F[Unit]

  def bracketCase[A, B](acquire: F[A])(use: A => F[B])(
    release: (A, Outcome[F, E, B]) => F[Unit]
  ): F[B]

  def uncancelable[A](body: Poll[F] => F[A]): F[A]
}

/*
- Безопасная работа с ошибками
- Вещи, которые должны быть закрыты, будут закрыты
 */

import cats.effect.std.Console
import cats.syntax.applicative._
import cats.syntax.flatMap._

type File = String

def openFile[F[_]: Applicative]: F[File] = "File.txt".pure[F]
def closeFile[F[_]: Console](f: File): F[Unit] =
  Console[F].println(s"File $f closed")
def writeFile[F[_]: Console](f: File, str: String): F[Unit] =
  Console[F].println(s"Write $str to $f")

type MonadThrow[F[_]] = kernel.MonadCancel[F, Throwable]

def program[F[_]: MonadThrow: Console]: F[Unit] =
  kernel.MonadCancel[F, Throwable]
    .bracketCase[File, Unit](openFile[F])(writeFile[F](_, "Hello!")) {
      case (file, Outcome.Succeeded(_)) =>
        writeFile(file, "Succeeded") >> closeFile[F](file)
      case (file, Outcome.Errored(_)) =>
        writeFile(file, "Errored") >> closeFile[F](file)
      case (file, Outcome.Canceled()) =>
        writeFile(file, "Canceled") >> closeFile[F](file)
    }

import cats.effect.IO

def cancelProgram: IO[Unit] =
  program[IO].start.flatMap(_.cancel)

program[IO].unsafeRunSync()

/*
### `Unique`

Тайпкласс, который позволяет создавать уникальные токены
 */

trait Unique[F[_]] {
  def unique: F[Unique.Token]
}

object Unique {
  final class Token
}

import cats.syntax.apply._
import cats.syntax.eq._

val token: IO[kernel.Unique.Token] = kernel.Unique[IO].unique
(token, token).mapN((x, y) => x === y).flatMap(IO.println).unsafeRunSync() // Always false

/*
### `Spawn`

Тайпкласс для создания файберов:
 */

import cats.effect.Fiber

trait Spawn[F[_]] extends MonadCancel[F, Throwable] with Unique[F] {

  def start[A](fa: F[A]): F[Fiber[F, Throwable, A]]
  def racePair[A, B](fa: F[A], fb: F[B]): F[Either[
    (Outcome[F, Throwable, A], Fiber[F, Throwable, B]),
    (Fiber[F, Throwable, A], Outcome[F, Throwable, B])
  ]]

  def never[A]: F[A]
  def cede: F[Unit]
}

/*
- `start` - создание файбера
- `racePair` - проведение гонки между файберами
- `never` - создание "бесконечного", но отменяемого файбера
- `cede` - превентивная отдача контроля другим файберам

Закон для `never`:
 */

import cats.effect.syntax.spawn._

def left[F[_]: kernel.Spawn]: F[Unit] =
  kernel.Spawn[F].never[Unit].start.flatMap(_.cancel) // Do nothing

left[IO].unsafeRunSync()

import cats.effect.IO

import scala.concurrent.duration._

(for {
  target <- IO.println("Catch me if you can!").foreverM.start
  _      <- IO.sleep(10.millis)
  _      <- target.cancel
} yield ()).unsafeRunSync()

/*
- `cede` подсказка для рантайма
- файбер отдает контроль исполнения рантайму
- можно реализовать прерывание длинных синхронных вычислений

### `Concurrent`

Тайпкласс для общения между файберами

 */

import cats.effect.{Deferred, Ref}

trait Concurrent[F[_]] extends Spawn[F] {
  def ref[A](a: A): F[Ref[F, A]]
  def deferred[A]: F[Deferred[F, A]]
}

val action: IO[String] = IO.println("This is only printed once").as("action")

val x: IO[String] = for {
  memoized <- action.memoize
  res1     <- memoized
  res2     <- memoized
} yield s"res1, res2 = $res1, $res2"

x.unsafeRunSync()

/*
- Создание уже известных `Ref` и `Deferred`

Помимо этого, `Concurrent` позволяет запускать параллельные вычисления, ограниченные
лимитом:
 */

import cats.Traverse

trait Concurrent1[F[_]] {
  def parTraverseN[T[_]: Traverse, A, B](n: Int)(ta: T[A])(
    f: A => F[B]
  ): F[T[B]]
}
/*
Он реализован поверх семафора
 */
import cats.effect.syntax.concurrent._

case class Request(body: String)
case class Response(body: String)

def callService(req: Request): IO[Response] = ???

def batchCall(requests: Vector[Request]): IO[Vector[Response]] =
  requests.parTraverseN(10)(callService)
/*
Только 10 одновременных запросов к сервису, все остальные запросы будут ждать своего доступа

### `Clock`

Тайпкласс для работы со временем рантайм платформы
 */

import scala.concurrent.duration.FiniteDuration

trait Clock[F[_]] {
  // java.lang.System.nanoTime
  def monotonic: F[FiniteDuration]

  // java.lang.System.currentTimeMillis
  def realTime: F[FiniteDuration]
}

(for {
  monotonic <- kernel.Clock[IO].monotonic
  realTime  <- kernel.Clock[IO].realTimeInstant
  _         <- IO.println(s"monotonic = $monotonic, realTime = $realTime")
} yield ()).unsafeRunSync()

/*
### `Temporal`

Тайпкласс для откладывания вычислений во времени
 */

trait Temporal[F[_]] extends Concurrent[F] with Clock[F] {
  def sleep(time: FiniteDuration): F[Unit]
}

(
  kernel.Clock[IO].monotonic.flatMap(fd => IO.println(s"First action $fd")) >>
  kernel.Temporal[IO].sleep(1.second) >>
  kernel.Clock[IO].monotonic.flatMap(fd => IO.println(s"Second action $fd"))
).unsafeRunSync()

/*
### `Sync`

Тайпкласс для откладывания любых синхронных вычислений
 */

trait Sync[F[_]]
    extends MonadCancel[F, Throwable] with Clock[F] with Unique[F] {

  def suspend[A](hint: Sync.Type)(thunk: => A): F[A]
}

object Sync {
  enum Type {
    case Delay, Blocking, InterruptibleOnce, InterruptibleMany
  }
}

/*
- `Delay` будет исполнен на обычном пуле
- `Blocking` будет исполнен на блокирующем пуле
- `InterruptibleOnce` прервет исполнение заблокированного потока через `Thread.interrupt`
в случае отмены файбера
- `InterruptibleMany` будет прерывать исполнение несколько раз заблокированного потока
через `Thread.interrupt` в случае отмены файбера
 */

import scala.io.Source
import scala.util.Using

kernel.Sync[IO].delay(println("Delayed action")).unsafeRunSync()
kernel.Sync[IO].blocking {
  Using(Source.fromFile("file"))(_.mkString)
} // blocking action

/*
## Async

Тайпкласс для откладывания любых асинхронных вычислений
 */

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

trait Async[F[_]] extends Sync[F] with Temporal[F] {
  def executionContext: F[ExecutionContext]
  def async_[A](k: (Either[Throwable, A] => Unit) => Unit): F[A]
  def evalOn[A](fa: F[A], ec: ExecutionContext): F[A]
}

def someFutureFunction(): Future[Unit] = Future(println("Hello from Future"))

val io = kernel.Async[IO].fromFuture {
  kernel.Sync[IO].delay(someFutureFunction())
} // Ничего не происходит

io.unsafeRunSync() // Вывод Hello from Future

// Реализация fromFuture
def fromFuture[F[_]: kernel.Async, A](fut: F[Future[A]]): F[A] =
  fut.flatMap { f =>
    kernel.Async[F].executionContext.flatMap { implicit ec =>
      kernel.Async[F].async_[A](cb => f.onComplete(t => cb(t.toEither)))
    }
  }

/* Сделать задание TypeClasses.scala */
