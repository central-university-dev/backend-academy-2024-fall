// Коллекции

// PartialFunction

/*
Частичная функция - это специальная функция, которая определена не на всем домене значений
Ее можно реализовать с помощью неполного паттерн-матчинга
 */

val f: PartialFunction[Int, Int] =
{ case x if x == 2 => x }

f(2)
f(1) // ошибка

// Виды коллекций

/*
1) Неизменяемые
     - Пакет scala.collection.immutable
     - Каждое изменение коллекции - создание новой копии
2) Изменяемые
     - Пакет scala.collection.mutable
     - Каждое изменение коллекции - изменение состояния текущей коллекции
3) Одновременно изменяемые и неизменяемые
     - Пакет scala.collection
     - На практике не используются, не будем касаться
 */

// Неизменяемые

/*
[Посмотреть](collections-immutable-diagram-213.svg)

Iterable

- Должен уметь возвращать итератор от коллекции
 */

trait Iterable[+A] {
  def iterator: Iterator[A]
}

/*
Iterator

- Итератор по коллекции. Можно получить все элементы коллекции один раз
- Каждое продвижение по коллекции изменяет итератор
- next() может выбросить NoSuchElementException
 */

trait Iterator[+A] {
  def hasNext: Boolean
  def next(): A
}

// def map[B](f: A => B): Iterable[B]
List(1, 2, 3).map(_ + 1)
// def flatMap[B](f: A => Iterable[B]): Iterable[B]
List(1, 2, 3).flatMap(x => List(x, x))
// def collect[B](pf: PartialFunction[A, B]): Iterable[B]
List(1, 2, 3).collect { case x if x == 2 => x }

// def isEmpty: Boolean
List.empty[Int].isEmpty
// def nonEmpty: Boolean
List.empty[Int].nonEmpty
// def size: Int
List.empty[Int].size
// def knownSize: Int
List.empty[Int].knownSize

// def head: A
List(1, 2).head
// def last: A
List(1, 2).last
// def headOption: Option[A]
List(1, 2).headOption
// def lastOption: Option[A]
List(1, 2).lastOption
// def find(f: A => Boolean): Option[A]
List(1, 2).find(_ % 2 == 1)

// def tail: Iterable[A]
List(1, 2).tail
// def init: Iterable[A]
List(1, 2).init
// def slice(from: Int, until: Int): Iterable[A]
List(1, 2).slice(0, 1)
// def take(n: Int): Iterable[A]
List(1, 2).take(3)
// def drop(n: Int): Iterable[A]
List(1, 2).drop(3)
// def takeWhile(p: A => Boolean): Iterable[A]
List(1, 2, 1).takeWhile(_ % 2 == 1)
// def dropWhile(p: A => Boolean): Iterable[A]
List(1, 2, 1).dropWhile(_ % 2 == 1)
// def filter(p: A => Boolean): Iterable[A]
List(1, 2, 1).filter(_ % 2 == 1)
// def filterNot(p: A => Boolean): Iterable[A]
List(1, 2, 1).filterNot(_ % 2 == 1)

// def splitAt(n: Int): (Iterable[A], Iterable[A])
List(1, 2).splitAt(1)
// def span(f: A => Boolean): (Iterable[A], Iterable[A])
List(1, 2, 1).span(_ == 1)
// def partition(p: A => Boolean): (Iterable[A], Iterable[A])
List(1, 2, 1).partition(_ == 1)
// def partitionMap[A1, A2](f: A => Either[A1, A2]): (Iterable[A1], Iterable[A2])
List(1, 2, 1).partitionMap(Left(_))

val list = List(("one", 1), ("two", 2))
// def groupBy[K](f: A => K): Map[K, Iterable[A]]
list.groupBy { case (id, _) => id }
// def groupMap[K, B](key: A => K)(f: A => B): Map[K, Iterable[B]]
list.groupMap { case (id, _) => id } { case (_, v) => v}
// def groupMapReduce[K, B](key: A => K)(f: A => B)(reduce: (B, B) => B): Map[K, B]
list.groupMapReduce { case (id, _) => id } { case (_, v) => v} (_ + _)

// def exists(f: A => Boolean): Boolean
List(1, 2, 3).exists(_ != 1)
// def forall(f: A => Boolean): Boolean
List(1, 2, 3).forall(_ == 1)
// def count(f: A => Boolean): Int
List(1, 2, 3).count(_ < 3)

// def foldLeft[B](z: B)(op: (B, A) => B): B
List(1, 2, 3).foldLeft(1)(_ + _)
// def foldRight[B](z: B)(op: (A, B) => B): B
List(1, 2, 3).foldRight(1)(_ + _)
// def reduceLeft[B >: A](op: (B, A) => B): B
List(1, 2, 3).reduceLeft(_ + _)
// def reduceRight[B >: A](op: (A, B) => B): B
List(1, 2, 3).reduceRight(_ + _)

// def mkString(start: String, sep: String, end: String): String
List(1, 2, 3).mkString("[", ", ", "]")

/*
Seq

Seq определяет коллекции, у которых есть длина (length) и элементы проиндексированы с 0
 */

trait Seq[+A] extends Iterable[A] with PartialFunction[Int, A] {
  def apply(i: Int): A
  def length: Int
  def isDefinedAt(idx: Int): Boolean
  def indices: Range
  def lengthCompare(len: Int): Int
}

List(1, 2, 3)(1)
List(1, 2, 3).length
List(1, 2, 3).isDefinedAt(4)
List(1, 2, 1).indices
List(1, 2, 1).lengthCompare(4)

// def indexOf[B >: A](elem: B, from: Int): Int
List(1, 2, 1).indexOf(1, 0)
// def lastIndexOf[B >: A](elem: B, end: Int = length - 1): Int
List(1, 2, 1).lastIndexOf(1)
// def indexWhere(p: A => Boolean, from: Int): Int
List(1, 2, 1).indexWhere(_ == 1, 0)
// def lastIndexWhere(p: A => Boolean, end: Int): Int
List(1, 2, 1).lastIndexWhere(_ == 1, 3)

// def prepended[B >: A](elem: B): Seq[B]
List(1, 2, 3).prepended(0)
// def prependedAll[B >: A](prefix: Seq[B]): Seq[B]
List(1, 2, 3).prependedAll(List(-1, 0))
// def appended[B >: A](elem: B): Seq[B]
List(1, 2, 3).appended(4)
// def appendedAll[B >: A](suffix: Seq[B]): Seq[B]
List(1, 2, 3).appendedAll(List(4, 5))

// def updated[B >: A](index: Int, elem: B): Seq[B]
List(1, 2, 3).updated(0, -1)
// def patch[B >: A](from: Int, other: Seq[B], replaced: Int): Seq[B]
List(1, 2, 3).patch(0, List(1, 2), 1)

// def sortWith(lt: (A, A) => Boolean): Seq[A]
List(1, 2, 3).sortWith(_ - _ > 0)
// def sorted[B >: A](implicit ord: Ordering[B]): Seq[B]
List(3, 2, 1).sorted
// def sortBy[B](f: A => B)(implicit ord: Ordering[B]): Seq[B]
List(3, 2, 1).sortBy(x => -x)

// def reverse: Seq[A]
List(1, 2, 3).reverse

// def startsWith[B >: A](that: Seq[B], offset: Int = 0): Boolean
List(1, 2, 3).startsWith(List(1, 2))
// def endsWith[B >: A](that: Seq[B]): Boolean
List(1, 2, 3).endsWith(List(2, 3))
// def contains[A1 >: A](elem: A1): Boolean
List(1, 2, 3).contains(1)

/*
`Seq[A]` имеет два наследованных интерфейса:

- `LinearSeq[A]` - эффективные head и tail
- `IndexedSeq[A]` - эффективные apply и length

Конкретные имплементации неизменяемых коллекций
- List
- ArraySeq
- Vector
- Queue
- Range
- HashMap
*/

/*
List

- Обычный связанный список
- Наследует интерфейс LinearSeq

Сложность:

- O(1) для вставки в начало
- O(n) для вставки в конец
- O(1) для конкатенации
- O(1) для получения головы
- O(n) для получения случайного элемента по индексу
 */

val list1: List[Int] = 1 :: 2 :: 3 :: Nil // ::(1, ::(2, ::(3, Nil)))

def sum(list: List[Int]): Int = {
  @scala.annotation.tailrec
  def sumInner(list: List[Int], sum: Int): Int = {
    list match {
      case Nil     => sum
      case x :: xs => sumInner(xs, sum + x)
    }
  }
  sumInner(list, 0)
}

sum(list1)

/*
Vector
Сделан поверх дерева с количеством веток 32

Сложность:

- O(~1) для вставки в начало
- O(~1) для вставки в конец
- O(~1) для конкатенации
- O(~1) для получения головы
- O(~1) для получения случайного элемента по индексу

Дефолтная реализация для IndexedSeq

P.S. O(~1) значит "effectively constant time", подробнее можно почитать тут:
https://docs.scala-lang.org/overviews/collections-2.13/concrete-immutable-collection-classes.html#vectors
 */

/*
HashMap

- Схожа по структуре с Vector, деревья с ветками 32
- Использует .hashCode() из Java

Дефолтная имплементация Map
 */

import scala.collection.immutable.HashMap

val m = HashMap("one" -> 1, "two" -> 2)
m + ("three" -> 3)
m - "one"

/*
Все коллекции в Scala по умолчанию - неизменяемые

Более того, они практически все доступны в любом месте без явного импорта
Это работает за счет специального файла Predef.scala, который автоматически включаются в любой файл
 */
