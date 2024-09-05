

// Objects

object StringUtils:
  def truncate(s: String, length: Int): String = s.take(length)

StringUtils.truncate("123456789", 3)


// Companion objects

class Circle(val radius: Double):
  def area: Double = Circle.calculateArea(radius)

object Circle:
  private def calculateArea(radius: Double): Double = Pi * pow(radius, 2.0)

val circle1 = Circle(5.0)
circle1.area

// Мини-задачки на самопроверку

// 1. Создайте объект `Calculator`, который содержит методы для основных арифметических операций: сложения (`add`), вычитания (`subtract`), умножения (`multiply`) и деления (`divide`). Каждый метод должен принимать два целых числа и возвращать их соответствующий результат.
// 2. Создайте класс `Timer` и его компаньон-объект. Класс должен иметь один параметр конструктора `startTime`. В компаньон-объекте определите метод `apply`, который принимает начальное время и создает новый экземпляр `Timer`. В классе `Timer` определите метод `current`Time, возвращающий текущее время в миллисекундах, и метод `elapsedTime`, вычисляющий прошедшее время с момента создания таймера.