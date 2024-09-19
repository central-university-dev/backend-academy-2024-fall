val one: 1 = 1
val two: 2 = 2
val oneOrTwo: 1 | 2 = one
val int: Int = oneOrTwo

// generics are erased from runtime
val ints: List[Int] = List(1, 2, 3)
val strings: List[String] = List("one", "two", "three")
ints.getClass == strings.getClass // = true