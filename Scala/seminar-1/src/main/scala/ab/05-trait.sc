trait Animal:
  def name: String
  def sayHello: String = s"My name is $name"

abstract class Pet extends Animal: // тут можно рассказать, зачем есть trait, и при этом abstract class
  def greeting: String
  override def sayHello = s"My name is $name, I say $greeting"


class Cat(val age: Int) extends Pet {
  override def greeting: String = "meow"

  override def name: String = "Stepan"
}

val cat = Cat(5)
cat.age
cat.sayHello


// Анонимный класс
val snake = new Animal:
  override def name = "Shhh"

snake.sayHello

val animals: List[Animal] = List(cat, snake)

animals.foreach(a => println(s"${a.name}: ${a.sayHello}"))

// Мини-задачки на самопроверку

// 1. Создайте трейт `MusicalInstrument` с абстрактным методом `play`, который возвращает `String`
// 2. Создайте класс `Guitar` и `Piano`, которые реализуют трейт `MusicalInstrument`. Метод `play` должен возвращать для `Piano` строку "Playing the piano", а для `Guitar` - "Strumming the guitar"
// 3. Добавьте в трейт `MusicalInstrument` метод `tune`, который возвращает строку "Tuning the instrument". Создайте класс `Violin` как дополнительный музыкальный инструмент. Для класса `Guitar`, переопределите метод `tune` таким образом, чтобы метод выводил строку "Tuning the guitar strings".
// 4. Расширьте иерархию, создав трейт `StringInstrument` с методом `numberOfStrings`, который возвращает количество струн на инструменте. Имплементируйте этот трейт в классах `Guitar` и `Violin`. Создайте новый класс `Drum` реализующий `MusicalInstrument` и исследуйте, что будет, если попытаться применить `StringInstrument` к этому классу.

