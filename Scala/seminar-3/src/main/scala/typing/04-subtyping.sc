trait Animal

trait Meow:
  def meow: String = "Meow!"

trait Bark:
  def bark: String = "Bark!"

class Cat extends Animal with Meow
class Dog extends Animal with Bark

val cat: Cat = new Cat
val animalCat: Animal = cat
val dog: Dog = new Dog

val pokemon: Cat | Dog | Animal = if ??? then cat else dog // what the type is?

def consumeAnimal(animal: Animal): Unit = ()

consumeAnimal(cat) // ok
consumeAnimal(dog) // ok
