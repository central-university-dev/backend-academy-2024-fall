import cats.effect.{Deferred, IO, IOApp, Ref}
import cats.syntax.parallel._

import scala.concurrent.duration._

case class Cache[K, V](ref: Ref[IO, Map[K, Deferred[IO, V]]])

object CacheExample extends IOApp.Simple {

  private def computeOnce[K, V](cache: Cache[K, V])(k: K, fa: IO[V]): IO[V] = {
    // Сначала получаем текущее состояние кеша
    for {
      currentCache <- cache.ref.get
      // Проверяем, существует ли уже отложенный результат для данного ключа k
      maybeDeferred = currentCache.get(k)
      // Если результат существует, возвращаем его
      result <- maybeDeferred match {
        // Если отложенное значение уже есть, ждем его
        case Some(deferred) => deferred.get
        case None =>
          // Если значения еще нет, создаем новый Deferred
          for {
            deferred <- Deferred[IO, V]
            // Пытаемся атомарно обновить кеш, добавляя новый отложенный результат
            _ <- cache.ref.update(_.updated(k, deferred))
            // Выполняем вычисление (fa)
            value <- fa
            // Завершаем Deferred с вычисленным значением
            _ <- deferred.complete(value)
          } yield value
      }
    } yield result
  }

  // Имитация длительной операции
  def expensiveComputation(key: Int): IO[Int] = {
    IO.sleep(2.seconds) *> IO(key.hashCode) // Например, используем hashCode как результат
  }

  def run: IO[Unit] = for {
    // Создаем начальное состояние кеша
    initialCache <- Ref.of[IO, Map[Int, Deferred[IO, Int]]](Map.empty)
    cache = Cache(initialCache)

    // Параллельно выполняем вычисления для одного и того же ключа
    results <- (1 to 5).toList.parTraverse { key =>
      computeOnce(cache)(1, expensiveComputation(key))
    }
    newResult <- computeOnce(cache)(1, expensiveComputation(10))

    // Печатаем результаты
    _ <- IO(println(s"Results: $results, and newResult: $newResult"))
  } yield ()
}