// Тайпклассы

/*
Тайпкласс (type class) - это паттерн проектирования, который позволяет добавлять новую функциональность к
существующим классам без их модификации. Тайпклассы создают абстракцию для работы с различными типами
данных, что позволяет поддерживать множество типов с общими операциями.
*/

// Объявление тайпкласса Show, представляющего функцию для преобразования типа A к строке.
trait Show[A] {
  def show(value: A): String
}

// Реализация для типа String
val stringShow: Show[String] = (value: String) => value

// Реализация для типа Int
val intShow: Show[Int] = (value: Int) => value.toString

stringShow.show("Hello, Scala!")
intShow.show(123)

/*
Если сравнивать с полиморфизмом через наследование, то такой подход дает больше гибкости для работы,
поскольку определение экземпляра тайпкласса и типа разделены
 */

def showToString(a: Object): String = a.toString

def show[T](a: T)(show: Show[T]): String = show.show(a)

showToString("Hello")
show("Hello")(stringShow)


/*
Некоторые тайпклассы обладают своими законами. Законы тайпкласса (laws of a type class) - это набор
концепций или правил, которые должны соблюдаться реализациями конкретного тайпкласса для обеспечения
корректной работы и согласованности интерфейса. Эти законы определяют ожидания от поведения методов
тайпкласса и проверяют корректность его реализаций.

Рассмотрим на примере отношения строгого порядка:
*/

trait StrictOrdering[T] {
  /** Принадлежит ли пара отношению */
  def <(x: T, y: T): Boolean
}

/*
Тогда, у этого тайпкласса будут законы:

- Антирефлексивность: Не существует такого a, что a < a.
- Антисимметричность: Для любых элементов a и b, если a < b и b < a, то a = b.
- Транзитивность: Для любых элементов a, b и c, если a < b и b < c, то a < c.

Как известно, отношение "меньше", на множестве вещественных чисел является отношением строгого порядка,
тогда определим его экземпляр
*/

val intStrictOrdering: StrictOrdering[Int] = (x: Int, y: Int) => x < y

intStrictOrdering.<(0, 1)
intStrictOrdering.<(1, 0)
intStrictOrdering.<(0, 0)

/*
Если мы захотим использовать данный тайпкласс в функции, то нам нужно будет каждый раз передавать его
руками, что делает использование неудобным
 */

def sortList[T](list: List[T])(strictOrdering: StrictOrdering[T]): List[T] =
  list.sorted(Ordering.fromLessThan(strictOrdering.<))

sortList(List(1, 3, 2))(intStrictOrdering)

/*
Поэтому, для автоматической подстановки экземпляров тайпкласса, есть специальные ключевые слова given\using

Давайте определим экземпляр типа StrictOrdering[Double] и напишем метод, который будет его использовать
*/

given doubleStrictOrdering: StrictOrdering[Double] = (x: Double, y: Double) => x < y

def sort[T](list: List[T])(using strictOrdering: StrictOrdering[T]): List[T] =
  sortList(list)(strictOrdering)

sort(List(1.1, 3.3, 2.2)) // doubleStrictOrdering подставилось автоматически

// Так же, есть специальный синтаксис для сокращения

def sortContextBound[T: StrictOrdering](list: List[T]): List[T] = sort(list)

/*
Может возникнуть вопрос: как ищутся экземпляры для подстановки, для этого есть простой алгоритм:

- Локально/Импорты
- Объекты-компаньоны

Рассмотрим на примере:
*/

trait Adder {
  def sum(a: Int, b: Int): Int
}

object Adder {
  implicit val adder: Adder = (a, b) => a + b
}

def increment(a: Int)(implicit adder: Adder): Int =
  adder.sum(a, 1)

object Adders {
  implicit val defaultAdder: Adder = (a, b) => a + b
}

implicit val localAdder: Adder = (a, b) => a + b

increment(1)

/*
Так же можно выводить экземпляры на основании существующих:
*/

given optionStrictOrdering[T](using strictOrdering: StrictOrdering[T]): StrictOrdering[Option[T]] with {
  override def <(x: Option[T], y: Option[T]): Boolean = (x, y) match {
  case (None, None) => false
  case (None, _) => true
  case (_, None) => false
  case (Some(x), Some(y)) => strictOrdering.<(x, y)
}
}

sort(List(Some(1.1), None, Some(3.3), None, Some(2.2)))



