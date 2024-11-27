/*
Мы уже давно знакомы с IO и на прошлом семинаре познакомились с Future, может возникнуть вопрос:
зачем нам использовать IO из внешней библиотеки, если уже есть Future. Для этого давайте сравним
данные эффекты и поймем разницу.
 */

/*
Future - представляет собой асинхронное вычисление:

- Есть в стандартной библиотеке
- Вычисляется жадно, сразу при инициализации
- Запоминает результат
- Не является монадой, потому что:
- Нет ссылочной прозрачности (то есть нельзя заменить функцию её результатом, то есть они не чистые)
Пример:
 */

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

val future = Future {
  println("I’m a Future") // Начал исполняться
}

val unitF =
  for (_ <- future; _ <- future) yield () // Сайд эффект будет вызван единожды

Await.result(unitF, 5.seconds)

/*
В то время, как cats.effect.IO:

- Нужно подключать из библиотеки
- Вычисляется лениво (только после явного запуска)
- Не запоминает результат вычислений
- Чистый и неизменяемый, есть ссылочная прозрачность
- Есть как синхронные, так и асинхронные эффекты
 */

import cats.effect.IO
import cats.effect.unsafe.implicits.global

val io = IO {
  println("I’m a IO") // Ничего не происходит
}

val unitIO =
  for (_ <- io; _ <- io) yield () // Ничего не происходит

unitIO.unsafeRunSync() // Сайд эффект будет вызван дважды

// Разница в выводе sequence

val futures = (1 to 10).map(i => Future(println(s"Processed $i")))

Future.sequence(futures)

import cats.effect.unsafe.implicits.global
import cats.syntax.traverse._

val ios = (1 to 10).toList.map(i => IO.println(s"Processed $i"))

ios.sequence.unsafeRunSync()
