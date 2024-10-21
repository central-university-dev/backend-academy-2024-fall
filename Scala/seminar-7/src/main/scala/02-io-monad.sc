// Введем специальный тип для функции: () => T
trait SimpleIO[+T]:
  def impureCompute(): T

def printLine(line: String): SimpleIO[Unit] = () => println(line)

printLine("Hello, world!") // ничего не делает
printLine("Hello, world!").impureCompute() // печатает в консоль

//  Такое определение выдерживает свойство ссылочной прозрачности:
val a = printLine("Hello, world!")
a
a
// Оба этих определения - одинаковые. И оба ничего не делают

/*
  Такой тип очень часто называют отложенным вычислением

  Он ленивый, потому что он ничего не запускает в момент своего создания, только в момент явного старта.

  Такие вычисления не мемоизируются и повторяются на каждый вызов, в отличие от lazy val

  impureCompute вызовы обычно лимитированы несколькими местами в программе, чаще всего одним - границей мира

 */

// Проверка: какие из этих функци чистые?

// 1.
def readln(): String = scala.io.StdIn.readLine()

// 2
def readLineDef: SimpleIO[String] =
  () => scala.io.StdIn.readLine()

// 3
val readLine: SimpleIO[String] =
  () => scala.io.StdIn.readLine()

// Как мы определяем чистую функцию с аргументом, которая будет читать пользовательский ввод в консоль
// и печатать аргумент и пользовательский ввод?

// Не чистая функция

def echo(line: String): Unit =
  val userInput = scala.io.StdIn.readLine()
  println(line)
  println(userInput)

// В сигнатуре функции есть side effect, но внутри используются не чистые вычисления

def echo2(line: String): SimpleIO[Unit] =
  () =>
    val userInput = scala.io.StdIn.readLine()
    println(line)
    println(userInput)

// Функция с чистыми вычислениями, однако они выполняются в момент вызова, а не на границе мира

def echo3(line: String): SimpleIO[Unit] =
  () =>
    val userInput = readLine.impureCompute()
    printLine(line).impureCompute()
    printLine(userInput).impureCompute()

// В прошлом слайде мы определили монадические преобразования WorldTransformer, с IO можно поступить так же

trait IO[+T]:
  def impureCompute(): T

  def flatMap[O](f: T => IO[O]): IO[O] =
    () =>
      val intermediate: T = impureCompute()
      f(intermediate).impureCompute()

  def map[O](f: T => O): IO[O] =
    () => f(impureCompute())

def printLineIO(line: String): IO[Unit] = () => println(line)
def readLineIO: IO[String] = () => scala.io.StdIn.readLine()

def echo4(line: String): IO[Unit] =
  for {
    userInput <- readLineIO
    _ <- printLineIO(line)
    _ <- printLineIO(userInput)
  } yield ()

// Но мы не можем написать echo4.impureCompute(),
// у нас нет реализации impureCompute для любой программы с типом IO

// Написать такую реализацию нетривиальная задача, поэтому мы воспользуемся готовой библиотекой cats-effect
