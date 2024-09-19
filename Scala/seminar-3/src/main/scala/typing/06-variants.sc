trait Animal {
  def name: String
}

trait Meow:
  def meow: String = "Meow!"

trait Bark:
  def bark: String = "Bark!"

class Cat(val name: String) extends Animal with Meow
class Dog(val name: String) extends Animal with Bark

val cat: Cat = new Cat("Murzik")
val dog: Dog = new Dog("Sharik")

// invariant

case class MutableBox[T](var inner: T)

val catBox: MutableBox[Cat] = MutableBox(cat)
val boxAnimal: MutableBox[Animal] = catBox // error
catBox.inner = dog // error

// covariant

case class CovariantBox[+T](inner: T)

val catBox: CovariantBox[Cat] = CovariantBox(cat)
val boxAnimal: CovariantBox[Animal] = catBox // ok

// contravariant

trait Serializer[-T]:
  def serialize(t: T): String

val catSerializer: Serializer[Cat] = new Serializer[Cat]:
  override def serialize(t: Cat): String = s"""{"cat-do": ${t.meow}}"""

val animalSerializerWrong: Serializer[Animal] = catSerializer // error

val animalSerializer: Serializer[Animal] = (animal: Animal) => animal.name
val dogSerializer: Serializer[Dog] = animalSerializer

animalSerializer.serialize(dog)
dogSerializer.serialize(dog)
dogSerializer.serialize(cat) // error

// more complex example

val func: Any => Dog = (any: Any) => new Dog(any.toString)

val transformFunc: Int => Animal = func
