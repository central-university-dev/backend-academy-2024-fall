
class Pet(var name: String)

val cat = new Pet("Stepan")
cat.name

// scala 3 only
val cat2 = new Pet("Stepan")


class Person(val firstName: String, val lastName: String = "Sidorov", constructorArgument: String = "") {

    println("initialization begins " + constructorArgument)
    val fullName = firstName + " " + lastName

    // a class method
    def printFullName: Unit =
        // access the `fullName` field, which is created above
        println(fullName)

    printFullName
    println("initialization ends")

  // auxiliary constructor
    def this(lastName: String) =
        this(firstName = "Pets") // use default parameter value
}

val p = Person("Robert", "Zimmerman", "Hello")
p.printFullName
// no p.constructorArgument


// Мини-задачки на самопроверку

// 1. Создайте класс `BankAccount`, который представляет банковский счет. Класс должен иметь приватные поля `accountNumber` и `balance`. Реализуйте методы для получения текущего баланса (`getBalance`) и для внесения/снятия денег (`deposit` and `withdraw`). Попытки снять больше денег, чем есть на счету, должны игнорироваться.
// 2. Модифицируйте класс `Person`, добавив поле `age`, добавив основной конструктор, который принимает имя, фамилию и возраст. Добавьте вторичный конструктор, который принимает только имя и фамилию, а возраст по умолчанию равен 0. Определите метод `isAdult`, который возвращает `true`, если возраст больше или равен 18.
// 3. Создайте класс `Library`, который имеет коллекцию книг. Внутри `Library`, определите класс `Book`, который имеет свойства `title` и `author`. Оба свойства должны быть приватными. В классе `Library` реализуйте методы для добавления книг (`addBook`) и получения информации о книгах (`getBookInfo`). Метод `getBookInfo` должен возвращать информацию о книгах в форме строки.
