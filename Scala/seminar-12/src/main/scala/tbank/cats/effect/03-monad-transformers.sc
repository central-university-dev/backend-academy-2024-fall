/*
Каждая монада нам что-то говорит про производимые вычисления

Рассмотрим на примере монад, которые знаем:

Monad	Effect
Option	Вычисление может ничего не вернуть
Either	Вычисление может упасть с ошибкой
List	Вычисление имеет несколько значений
 */

/*
Тогда, рассмотрим новые монады

Monad	Effect
Writer	Вычисление имеет аккумулятор
Reader	Вычисление имеет доступ к неизменяемому контексту
State	Вычисление имеет доступ к изменяемому контексту
 */

/*
Writer: Предоставляет удобный api монады для работы с парой.
 */

import cats.data.Writer

def sumWithLog(list: List[Int]): Writer[String, Int] =
  list.foldLeft(Writer("0", 0)) { case (w, v) =>
    w.flatMap(s => Writer(s" + $v", s + v))
  }

sumWithLog(List(1, 2, 3)).run // (0 + 1 + 2 + 3,6)

/*
Аналогичные вычисления можно провести и без Writer'a
 */

def sumWithLogPair(list: List[Int]): (String, Int) =
  list.foldLeft(("0", 0)) { case ((acc, s), v) =>
    (acc + s" + $v", s + v)
  }

sumWithLogPair(List(1, 2, 3)) // (0 + 1 + 2 + 3,6)

/*
Reader: Предоставляет удобный api монады для работы с функцией A => B

Чаще всего используется для проброса конфигов или данных о запросе по
типу id запроса, другой мета-инфы
 */

import cats.data.Reader

def getOrDefault(
  getOption: Reader[Int, Option[String]],
  defaultVal: String
): Reader[Int, String] =
  getOption.map(_.getOrElse(defaultVal))

val r = Reader[Int, Option[String]](i => if (i < 10) Some(i.toString) else None)

println(getOrDefault(r, "-1").run(10)) // -1

/*
Аналогичные вычисления можно провести и без Reader'a
 */

def getOrDefaultFunc(
  getOption: Int => Option[String],
  defaultVal: String
): Int => String = { i => getOption(i).getOrElse(defaultVal) }

val f: Int => Option[String] = { i => if (i < 10) Some(i.toString) else None }

println(getOrDefaultFunc(f, "-1")(10)) // -1

/*
State: Предоставляет удобный api монады для работы с функцией A => (A, B)
 */

import cats.data.State

val head  = State[String, Char](s => (s.tail, s.head))
val toInt = State.inspect[String, Int](_.toInt)

(for {
  h  <- head
  no <- toInt
  newNo = no / 3
} yield s"$h$newNo")
  .run("#42")
  .value // ("42", "#14")

/*
В Scala Writer/Reader/State монады не включены в стандартную библиотеку.

Их можно найти в библиотеке cats, более подробное описание можно
найти в [доке](https://typelevel.org/cats).
 */

/*
Это все хорошо, мы рассмотрели много разных монад

Но что происходит на практике?

На практике сигнатура даже какой-то простой функции будет выглядеть как то так
 */

import cats.effect.IO

type UserId = String
type Config = String

case class User(firstName: String, lastName: String)

def findUser(userId: UserId): Reader[Config, IO[Option[User]]] = ???

/*
Как с этим работать?

Проблема в том, что для получения пользователя, придется делать 3 вызова
map/flatMap и соответственно код будет выглядеть ужасно
 */

def showUser(userId: UserId): Reader[Config, IO[Option[Unit]]] =
  findUser(userId).map { ioOptUser =>
    ioOptUser.map(optUser => optUser.map(user => println(user)))
  }

/*
При этом мы даже не можем записать данный код в виде for-comprehension, потому что
каждый стой имеет свой тип, а в for'e они должны быть одинаковыми

findUser: Reader[Config, IO[Option[User]]]
ioOptUser: IO[Option[User]]
optUser: Option[User]
user: User

Что с этим делать?

Для решения проблемы вложенности существуют так называемые монадические трансформеры

Наиболее популярные:

- `OptionT[F, A]`
- `EitherT[F, A, E]`
- `ReaderT[F, A, B]`
- `WriterT[F, L, V]`
- `StateT[F, S, A]`
 */

/*
В чем заключается их трансформация?

Если посмотреть внимательнее, то в нашей функции showUser нам не нужна информация
 о том, что у нас есть какой-то Config, мы никак не используем то, что наше
 вычисление лежит внутри IO

Если упростить код выше и забыть про существование конфига и IO, то наша функция
будет выглядеть просто
 */

def findUserOption(userId: UserId): Option[User] = Some(User("Petr", "Ivanov"))

def showUserOption(userId: UserId): Option[Unit] =
  findUserOption(userId).map(println)

/*
Что делают трансформеры?

По факту трансформеры просто "схлопывают" нашу многоуровневую структуру до
обычной, чтобы с ней было удобно работать
 */

/*
Почему трансформеры монадические?

Они выводят инстансы монады и других тайпклассов на основе имеющихся у F

 */

// implicit def readerMonad[F[_]: Monad, A]: Monad[ReaderT[F, A, *]]

/*
Показать картинку transformers.jpg

Теперь перепишем наш пример с трансформерами
 */

import cats.data.{OptionT, ReaderT}

type OptionIO[A] = OptionT[IO, A]

def findUserTransformed(userId: UserId): ReaderT[OptionIO, Config, User] = ???

def showUserTransformed(userId: UserId): ReaderT[OptionIO, Config, Unit] =
  findUserTransformed(userId).map(println)

/*
Но как теперь получить контекст?
 */

def showUserWithContext(userId: UserId): ReaderT[OptionIO, Config, Unit] =
  for {
    user   <- findUserTransformed(userId)
    config <- ReaderT.ask[OptionIO, Config]
  } yield println(user)

/*
Как реализован ReaderT.ask?

Если обратить внимание, то у нас появилась проблема так как не все функции
будут возвращать одинаковый тип мы не сможем их комбинировать через
flatMap/for-comprehension
 */

def process(user: User): ReaderT[IO, Config, User] = ???
def print(user: User): IO[Unit]                    = ???

def showUserLift(userId: UserId): ReaderT[OptionIO, Config, Unit] =
  for {
    user          <- findUserTransformed(userId)
    processedUser <- process(user).mapF(OptionT.liftF)
    _ <- ReaderT.liftF(
           OptionT.liftF(print(processedUser))
         )
  } yield ()

// Задание: написать простую реализацию ReaderT (MEitherT.scala) и использовать ее в задании
