// Переменная
var x = 1
x = x + 1

// Значения
val y = 1
// y = y + 1

//  Примитивные типы
//  Boolean
//  Byte, Short, Int, Long
//  Float, Double
//  Char, String

// Числа

val bool = true
val byte: Byte = 1
val short: Short = 1
val int = 1
val long = 1L
val double = 1.0
val float = 1.0f

val hex = 0xabc
val longHex = 0xffffL

val p = .99
val exp = 1e-3
val veryBigNumber = 100_000_000_000L

val quintillion = BigInt(1_000_000_000_000_000_000L)
val pi = BigDecimal(3.141592653589793238)

// Строки

val c = 'a'
val s = s"5 + 3 = ${5 + 3}, v=$float"
val multiline =
  """first
    |second
    |third
    |""".stripMargin

// Функции

def square(x: Int): Int = x * x

def hello(): Unit = println("Hello!")

def block(): Unit = {
  println("Hello!")
  println("Hello!")
}

val squareFunction: Int => Int = square

val notImplementedError = ???

// Мини-задачки на самопроверку

// Создайте иммутабельные переменные со значениями `1.0`, `1`, `1L`, `1.0F`
// Какой тип они будут иметь? Попробуйте явно указать тип этих переменных при создании
// Какие типы можно указать в каждом случае, а где возникает Type Mismatch Error?
// Безопасное приведение примитивных типов можно проверить в документации https://docs.scala-lang.org/tour/unified-types.html#type-casting

// Напишите функцию, которая принимает имя и возраст человека, а затем возвращает строку
// с приветствием, включающую имя и годы.
