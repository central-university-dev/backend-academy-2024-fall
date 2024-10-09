// F[_] и его производные позволяют нам использовать тайпклассы высшего порядка

// Рассмотрим несколько таких тайпклассов из cats-core.

import cats.{Functor => _, Applicative => _, Monad => _}

// Причем тут функтор и HKT?  Вспомним определение из лекции
/*
   Функтор – класс типов, для которых определен метод,
  позволяющий мутировать его внутренней состояние, сохраняя
  структуру, и выполняется ряд правил.
 */

trait Functor[F[_]]:
  def map[A, B](fa: F[A])(f: A => B): F[B]

/*
   Functor is a type class that abstracts over type constructors that can be mapped over
 */

// Законы
// Composition fa.map(f).map(g) = fa.map(f.andThen(g))
// Identity    fa.map(x => x) = fa

// Какие функторы вы знаете?

import cats.Functor
Functor[List].map(List(1, 2, 3))(_.toDouble)

import cats.syntax.functor.given
// это на самом деле метод из List, но вы поняли суть
List(1, 2, 3).map(_.toDouble)

// Applicative - функтор с методами ap и pure

import cats.{Functor => _}
trait Applicative[F[_]] extends Functor[F]:
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
  def pure[A](a: A): F[A]

  // из функтора
  def map[A, B](fa: F[A])(f: A => B): F[B] = ap(pure(f))(fa)

// У него есть еще 3 закона, которые тут рассматривать не будем
// https://typelevel.org/cats/typeclasses/applicative.html

import cats.Applicative
Applicative[Option].pure(1)

import cats.syntax.applicative.given
1.pure[Option]
import cats.syntax.apply.given
Some((x: Int) => x + 1).ap(Option[Int](1))

// Monad - аппликатив с методом flatten

trait Monad[F[_]] extends Applicative[F]:
  def flatten[A](ffa: F[F[A]]): F[A]

  // из аппликатива
  def map[A, B](fa: F[A])(f: A => B): F[B]

  // flatMap - map followed by flatten:
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] =
    flatten(map(fa)(f))

// можно наоборот - определить flatMap через flatten
// def flatten[A](ffa: F[F[A]]): F[A] =
//   flatMap(ffa)(fa => fa)

Option(Option(1)).flatten
Option(None).flatten
Some(1).flatMap(x => Some(x + 1))

// flatMap имеет особое значение в scala,
// поскольку for-comprehension использует этот метод для объединения операций в монадическом контексте

val list = List(1, 2, 3)

for elem <- list // это map
  yield elem

for
  elem1 <- list // это flatMap
  elem2 <- list // это тоже flatMap
  elem3 <- list // это map
yield (elem1, elem2, elem3)

// эквивалентная запись
list.flatMap: elem1 =>
  list.flatMap: elem2 =>
    list.map: elem3 =>
      (elem1, elem2, elem3)

// Какие монады вы знаете?

import cats.Monad
Monad[List].flatMap(List(1, 2, 3))(el => List(el, el))

import cats.syntax.flatMap.given
// опять-таки, это метод из List, но суть понятна
List(1, 2, 3).flatMap(elem => List(elem, elem))

/*
  Задание:

  1. Напишите небольшой ADT, например дерево
  2. Реализуйте для него тайпкласс Functor
  3. Убедитесь, что для него выполняются законы функтора с помощью cats-laws

  Задание со *:
    Реализуйте для этого ADT инстанс Monad
    В отличие от остальных тайпклассов вы столкнетесь с необходимостью реализовать tailRecM.
    Зачем это нужно лучше всего прочитать в документации https://typelevel.org/cats/typeclasses/monad.html
 */
