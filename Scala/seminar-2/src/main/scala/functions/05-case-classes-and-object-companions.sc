// Common class
class Person(val name: String, val age: Int):
  def this(name: String) {
    this(name, 0) // Default constructor with default age
  }
  override def toString: String = s"Person($name, $age)"
  def canEqual(a: Any): Boolean = a.isInstanceOf[Person]
  override def equals(that: Any): Boolean =
    that match
      case that: Person => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  override def hashCode: Int = this.name.length + this.age
end Person

// Object companion
object Person:
  def apply(name: String, age: Int): Person = new Person(name, age) // to remove new keyword when creating an instance
  def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age)) // to use pattern matching
end Person

val person1 = Person("Grisha", 26)
person1 match
  case Person(name, age) => println(s"Hello, $name! You are $age years old")

// Case class with the same functionality
case class PersonCaseClass(name: String, age: Int)
val person2 = PersonCaseClass("Bob", 25)

println(person1)
println(person2)

// Home tasks:
// 1. Create a class named `Movie` with attributes `title: String` and `year: Int`. Implement a companion object with an apply method for creating `Movie` objects without using the new keyword.
// 2. Implement an unapply method in the companion object of the `Movie` case class to extract the title and year from a `Movie` object for pattern matching.
// 3. Write a function in the companion object of the `Movie` class that increments the year of a `Movie` object by 1 and returns the updated object.
// 4. Create a `Director` case class with attributes `name: String` and `age: Int`.
// 6. Create a function in the `Director` companion object that takes a `Director` object and returns a new `Director` instance with the age increased by 5.
// 7. Test the above functions by creating instances of `Movie` and `Director` classes, applying the methods from their companion objects, and pattern matching using the unapply method.