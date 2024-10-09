package ru.tbank

import cats.kernel.laws.discipline.{MonoidTests, SemigroupTests}
import cats.syntax.semigroup.*
import cats.{Eq, Semigroup}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

class LawsSpec extends AnyFunSuite, FunSuiteDiscipline, Checkers {

  checkAll(
    "Semigroup Laws",
    SemigroupTests[List[Int]].semigroup
  )

  checkAll(
    "Monoid Laws",
    MonoidTests[List[Int]].semigroup
  )

  // пример тестирования полугруппы на своем типе
  // ADT дерева
  sealed trait Tree[+A]

  case object Leaf extends Tree[Nothing]

  case class Node[+A](p: A, left: Tree[A], right: Tree[A]) extends Tree[A]

  // реализация полугруппы для дерева
  given [T](using Semigroup[T]): Semigroup[Tree[T]] =
  (x: Tree[T], y: Tree[T]) =>
    (x, y) match
      case (Leaf, Leaf) => Leaf
      case (Leaf, Node(p, l, r)) => Node(p, l, r)
      case (Node(p, l, r), Leaf) => Node(p, l, r)
      case (Node(p1, l1, r1), Node(p2, l2, r2)) =>
        Node(p1 |+| p2, l1 |+| l2, r1 |+| r2)

  // реализация равенства для дерева
  given [T](using Eq[T]): Eq[Tree[T]] = Eq.fromUniversalEquals

  // реализация генератора для дерева
  given [T](using Arbitrary[T]): Arbitrary[Tree[T]] = Arbitrary(
    Gen.oneOf(
      Gen.const(Leaf),
      for
        e <- Arbitrary.arbitrary[T]
        hasLeft <- Arbitrary.arbitrary[Boolean]
        hasRight <- Arbitrary.arbitrary[Boolean]
        left <- if (hasLeft) Arbitrary.arbitrary[Tree[T]] else Gen.const(Leaf)
        right <- if (hasRight) Arbitrary.arbitrary[Tree[T]] else Gen.const(Leaf)
      yield Node(e, left, right)
    )
  )

  // тестирование полугруппы для дерева
  checkAll("Tree[Int].SemigroupLaws", SemigroupTests[Tree[Int]].semigroup)
  checkAll("Tree[Double].SemigroupLaws", SemigroupTests[Tree[Double]].semigroup)
  checkAll("Tree[String].SemigroupLaws", SemigroupTests[Tree[String]].semigroup)
}
