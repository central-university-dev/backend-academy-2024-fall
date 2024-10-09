def foldList[A](list: List[A])(empty: A)(f: (A, A) => A): A =
  list.fold(empty)(f) // какая-то имплементация

foldList(List(1, 2, 3))(0)(_ + _)

def foldVector[A](vector: Vector[A])(empty: A)(f: (A, A) => A): A =
  vector.fold(empty)(f)

def foldSet[A](set: Set[A])(empty: A)(f: (A, A) => A): A =
  set.fold(empty)(f)

// не работает, F не умеет принимать ничего внутрь
trait WrongFoldable[F]:
  def fold[A](foldable: F[A])(empty: A)(f: (A, A) => A): A

trait Foldable[F[_]]:
  def fold[A](foldable: F[A])(empty: A)(f: (A, A) => A): A

given Foldable[List] with
  def fold[A](foldable: List[A])(empty: A)(f: (A, A) => A): A =
    foldable.fold(empty)(f)

summon[Foldable[List]].fold[Int](List(1, 2, 3))(0)(_ + _)

// scala2 - repl -> :help kind
// scala3 - https://github.com/scala/scala3/issues/21655

// Kind 0 - это все примитивные типы, которые ни от чего не зависят
// Например: Int, String, Double
// Их можно представить как * или Type

// Kind 1 - это типы, которые зависят от типов с Kind 0 (generics)
// Например: List, Option
//  * -> * или Type -> Type

// Kind 2+:  Higher-Kinded Types, HKT
// Соответственно это типы, которые имеют тайп параметр с Kind 1
// Для обозначения конструктора HTK используется специальный синтаксис F[_], который можно представить как (Type -> Type)

// Для примера вспомним трейт Show. Какой у него kind?

import cats.Show
Show[Int]
Show[List[Int]]

// Show может принимать параметр с Kind 1, поэтому он может быть использован в качестве F[_] параметра
trait Name[F[_]]:
  def name: String

given Name[Show] with
  def name: String = "Show"

summon[Name[Show]].name

// Задания на проверку:

// Какой трейт будет иметь кайнд: Type -> Type -> Type?

trait Foo[A, B] // Например, Either

// Какой трейт будет иметь кайнд: (Type -> Type) -> Type?

trait Foo2[F[_]]

// Какой трейт будет иметь кайнд: (Type -> Type -> Type) -> Type?

trait Foo3[F[_, _]]

// Какой каинд у такого трейта?

trait Foo4[A, F[_], B]

// Type -> (Type -> Type) -> Type -> Type

// функция трансформации между типами
import cats.~>
import cats.arrow.FunctionK

// обычная функция
new Function1[Int, Int]:
  def apply(v1: Int): Int = v1 * 2

// короткая запись
val f = new (Int => Int):
  def apply(v1: Int) = v1 * 2

f(2)

// аналогично для типов
new FunctionK[List, Option]:
  def apply[A](fa: List[A]): Option[A] = fa.headOption

// короткая запись
val fk = new ~>[List, Option]:
  def apply[A](fa: List[A]): Option[A] = fa.headOption

fk(List(1,2,3))
