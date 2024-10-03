// Изменяемые коллекции

/*
Array

Специальная коллекция, которая реализована поверх Java массивов

Сложность:

O(1) для вставки в начало
O(n) для вставки в конец
O(n) для конкатенации
O(1) для получения головы
O(1) для получения случайного элемента по индексу
 */

val array = Array(1, 2, 3)
array(0) = 0

/*
ArrayBuffer

Динамический массив, по факту тоже самое, что и Array, но с амортизированной константой для вставки в конец
 */

import scala.collection.mutable.ArrayBuffer

val arrayBuffer = ArrayBuffer(1, 2, 3)
arrayBuffer += 4 // += на val

/*
HashMap и HashSet

Если нам требуется использовать в конкретном месте изменяемую коллекцию, достаточно импортировать весь
пакет scala.collection.immutable:
 */

import scala.collection.mutable._

val m = Map("one" -> 1)
m += ("two" -> 2)
// m = Map("one" -> 1, "two" -> 2)

