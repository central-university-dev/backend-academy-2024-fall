// в Scala есть более удобный механизм для работы с асинхронными вычислениями
import scala.concurrent.Future

// Future - значение, которое может быть получено где-то в будущем
// жадное исполнение - то есть выполняется сразу во время инициализации
// нужен execution context - пул соединений
// может завершиться с ошибкой

import scala.concurrent.ExecutionContext.Implicits.global

val somewhereOne: Future[Int] = Future {
  println("Hello 1!")
  1
}
val somewhereTwo: Future[Int] = Future {
  println("Hello 2!")
  2
}
println("Hi!")

// какой результат выполнения может быть у этого кода?

// а у этого?
object Quiz {

  import scala.concurrent.ExecutionContext.Implicits.global

  val somewhereOne: Future[Int] = Future.successful {
    println("Hello 1!")
    1
  }
  val somewhereTwo: Future[Int] = Future.successful {
    println("Hello 2!")
    2
  }
  println("Hi!")
}

// методы комбинаторы (map, flatMap и тд) принимают execution context, почему?
// каждое преобразование комбинатором создает новый поток в пуле, который выполняется только после того, как завершен предыдущий

// мемоизация у future
val asyncOne: Future[Int] = Future {
  println("Call only once")
  10
}
def executeAfterOne(in: Int): Future[Int] = Future{
  println(in + 1)
  in + 1
}
def executeAfterTwo(in: Int): Future[Int] = Future {
  println(in - 1)
  in - 1
}

val chainOne: Future[Int] =
  asyncOne.flatMap(executeAfterOne)
val chainTwo: Future[Int] =
  asyncOne.flatMap(executeAfterTwo)

// for comprehension для Future
def callDBOne: Future[Int] = ???
def callDBTwo: Future[Int] = ???

def logicParallel: Future[Int] = {
  val futureOne = callDBOne // задать вопрос, почему так?
  val futureTwo = callDBTwo

  for {
    resOne <- futureOne
    resTwo <- futureTwo
  } yield resOne + resTwo
}

import scala.util._

// callback
val toCallBack: Future[String] = Future {
  println("Doing smth")
  "Oh my!"
}

toCallBack.onComplete {
  case Success(value) => println(value)
  case Failure(exception) => println(s"Got an error: ${exception.getMessage}")
}