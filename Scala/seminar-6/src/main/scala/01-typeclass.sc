/*
 Повторим определение тайпкласса:

 Тайпкласс (type class) — это абстрактный параметризованный тип, который позволяет добавлять
 новое поведение к любому закрытому типу данных без использования подтипов (ad-hoc полиморфизм)

 Чаще всего это трейты с одним или несколькими параметрами, реализации которых предоставляются в виде экземпляров given

 */

// Уже знакомый пример тайпкласса Show

trait Show[A]:
  def show(a: A): String

case class Person(name: String, age: Int)
val person = Person("Bob", 22)

given showForPerson: Show[Person] with
  def show(p: Person): String =
    s"${p.name} of age ${p.age}"

showForPerson.show(person)

// Такой тайпкласс уже реализован в библиотеке cats-core:
//
// libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0"

import cats.Show

val x = 1d
Show[Double].show(x)

// Более удобная запись
import cats.syntax.show.given

x.show

// Помимо Show в cats-core есть реализации многих других тайпклассов,
// большая картинка - https://github.com/tpolecat/cats-infographic?tab=readme-ov-file

// Мы рассмотрим несколько самых популярных.

import cats.{Monoid, Semigroup}

// Semigroup - тайпкласс, который имеет ассоциативную бинарную операцию

trait Semigroup[A]:
  def combine(x: A, y: A): A

// Из определения следует закон ассоциативности:
// combine(x, combine(y, z)) = combine(combine(x, y), z)

// пример тестирования законов полугруппы для своего ADT в LawsSpec

// Вопрос: для каких из знакомых типов может существовать тайпкласс полугруппы?

Semigroup[String].combine("a", "b")
Semigroup[Int].combine(1, 3)

// Monoid - полугруппа с нейтральным значением

trait Monoid[A] extends Semigroup[A]:
  def empty: A

// Добавляется закон:
// combine(x, empty) = combine(empty, x) = x

Monoid[Int].empty
Monoid[String].combine(Monoid[String].empty, "value")

/*
  Задание:

  1. Напишите небольшой ADT, например свой Either
  2. Реализуйте для него тайпкласс Monoid как в примере ниже
  3. Убедитесь, что для него выполняются законы моноида с помощью cats-laws

 */

trait MyADT

given Monoid[MyADT] = new Monoid[MyADT]:
  override def empty: MyADT = ???
  override def combine(x: MyADT, y: MyADT): MyADT = ???
