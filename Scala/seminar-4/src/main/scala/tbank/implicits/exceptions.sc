// Непроверяемые исключения

/*
Показать [image](src/main/resources/java-exception-hierarchy.png)

Throwable - базовый класс того, что можно кинуть
Errors - обычно кидает JVM на какие-то критические ошибки, непроверяемые
Exception - базовый класс для проверяемых исключений
RuntimeException - базовый класс для непроверяемых исключений, в основном
 используем его и его потомков
*/

class SomeError extends Error("Something happened in JVM")
class SomeCheckedException extends Exception("Something checked happened")
class SomeUncheckedException extends RuntimeException("Something unchecked happened")
class SomeClass

// Throw
// Тут можно кинуть любую ошибку, кроме SomeClass
def divide(a: Int, b: Int): Int =
  if (b == 0) throw new SomeUncheckedException
  else a / b

divide(10, 2)
divide(10, 0)

// Try-catch
def divideUnchecked(x: Int, y: Int): Int =
  try {
    x / y
  } catch {
    case ex: ArithmeticException =>
      println("Divided by 0")
      throw ex
  }

divideUnchecked(10, 2)
divideUnchecked(10, 0)

// Проверяемые исключения

/*
Так же, с ошибками можно работать и явно, только в этом случае наше взаимодействие с ними
будет не через управляющие конструкции, а через тип возвращаемого значения из функции

Базовый вариант работы с ошибками - `Option[T]`, который может вернуть из функции значение
`Some[T]` или ничего `None`
 */

def divideOption(x: Int, y: Int): Option[Int] =
  Option.when(y != 0)(x / y)

divideOption(10, 2)
divideOption(10, 0)

/*
В данном примере, мы не можем узнать какая конкретно ошибка произошла в методе

Поэтому для результата работы функции, возвращающей ошибку есть специальный тип
`Either[L, R]`, который имеет 2 возможных типа значения: `Left[L]`, `Right[R]`

В качестве левого значения обычно выступает ошибка, которую мы можем вернуть из функции, а в
качестве правого, потенциальный результат
 */

def divideChecked(x: Int, y: Int): Either[ArithmeticException, Int] =
  if (y == 0) Left(new ArithmeticException("/ by zero"))
  else Right(x / y)

divideChecked(10, 2)
divideChecked(10, 0)

/*
Помимо `Either[L, R]`, для работы с проверяемой ошибкой в пакете `scala.util` есть тип `Try[T]`,
который имеет 2 возможных значения: `Failure[T]`, `Success[T]`. По факту, можно относиться к
этому типу, как к `Either[Throwable, T]`. Он примечателен тем, что у него есть конструктор,
который ловит все "не фатальные" ошибки и возвращает результат типа `Try`. К "фатальным"
ошибкам относятся различные ошибки JVM, которые не позволят продолжить корректную работу.
 */

import scala.util.Try

def divideTry(x: Int, y: Int): Try[Int] = Try(x / y)

divideTry(10, 2)
divideTry(10, 0)

/*
Идеальный вариант работы с ошибками - не допускать их. Так, в данном примере, мы можем
определить тип `NonZeroInt`, который не сможет принимать нулевое значение, тогда функция
деления не сможет выкинуть ошибку.
 */

object SafeDivide {
  opaque type NonZeroInt = Int

  object NonZeroInt {
    def safeCreate(value: Int): Option[NonZeroInt] =
      Option.when(value != 0)(value)
  }

  def divideSafe(x: Int, y: NonZeroInt): Int = x / y
}

import SafeDivide.*

NonZeroInt.safeCreate(2)
  .map(divideSafe(10, _))

NonZeroInt.safeCreate(0)
  .map(divideSafe(10, _))

// try-with-resources (Using)

/*
scala.util.Using - это объект-утилита для управления ресурсами связанными с AutoCloseable, 
такими как файлы, сетевые соединения, потоки и т. д. Он обеспечивает безопасное и
автоматическое управление ресурсами, освобождая их после завершения блока кода.
*/

import scala.util.Using
import java.io._

// Создаем файл и записываем в него данные
val file = new File("example.txt")
Using(new BufferedWriter(new FileWriter(file))) { writer =>
  writer.write("Hello, World!")
}

// Чтение данных из файла
Using(new BufferedReader(new FileReader(file))) { reader =>
  println(reader.readLine())
}

/*
В данном примере, блоки кода внутри Using автоматически закроют ресурсы (в данном случае
buffered-символьный поток для записи и чтения), когда выполнение блока завершится или при
возникновении исключения. Это гарантирует корректное управление ресурсами и избегает 
утечек ресурсов.
*/
