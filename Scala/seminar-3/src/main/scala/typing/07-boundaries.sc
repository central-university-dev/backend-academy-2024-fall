// upper type bounds
// T <: A - T is subtype of A

abstract class Animal:
  def name: String

abstract class Pet extends Animal

class Cat extends Pet:
  override def name: String = "Cat"

class Dog extends Pet:
  override def name: String = "Dog"

class Lion extends Animal:
  override def name: String = "Lion"

class PetContainer[P <: Pet](p: P):
  def pet: P = p

val dogContainer = PetContainer[Dog](Dog())
val catContainer = PetContainer[Cat](Cat())

val lionContainer = PetContainer[Lion](Lion()) // error
