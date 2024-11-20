// Функциональная обертка на AtomicReference - Ref
import cats.*
import cats.implicits.*
import cats.effect.*

// update и modify только при успешном выполнении, иначе повторяется
def example(ref: Ref[IO, Int]): IO[Int] =
  for {
    _ <- ref.update(_ + 1)
    prev <- ref.modify { count =>
      (count + 1, count)
    }
  } yield prev

import cats.effect.unsafe.implicits.global
val res = Ref.of[IO, Int](1).map(example).unsafeRunSync().unsafeRunSync()

// Механизм синхронизации - Deferred
// Deferred - ссылка на значение, которое будет заполнено в будущем
// get:
// - Если ссылка на заполнена, то будет производить семантическую блокировку файбера, пока значение не заполнено
// - Если значение уже есть, сразу возвращает значение
// - Можно отменять
// complete:
// - заполняет одним значением, которое разблокирует все заблокированные файберыр
// - заполняется один раз и возвращает true, все последующие вызовы не будут иметь эффект и будут возвращать false
def example(deferred: Deferred[IO, Int]): IO[Unit] =
  for {
    fib <- deferred.get.flatMap(v => IO.println(s"Got: $v")).start
    _ <- deferred.complete(1)
    _ <- deferred.complete(2)
    _ <- fib.join
  } yield () // Got: 1

// Deferred и Ref это база
// Реализация кэша
// показываем CacheExample - не надо, слишком сложно

// Semaphore - количественный ограничитель до ресурса
import cats.effect.std.Semaphore
import cats.effect.std.Console
val semaphore: IO[Semaphore[IO]] = Semaphore[IO](10)
semaphore
  .flatTap(_.acquireN(5)) // уменьшает кол-во счетчиков на 5
  .flatTap(_.releaseN(3)) // увеличивает кол-во счетчиков на 3
  .map(_.permit) // выдает доступ к ресурсу

// показываем пример в SemaphoreExample

// что под капотом
// Fiber - представляет собой легковесный поток, который позволяет выполнять
// вычисления асинхронно и управлять их жизненным циклом.
// С помощью Fiber можно запускать задачи, отменять их и ожидать их завершения.

// показываем пример в FiberExample