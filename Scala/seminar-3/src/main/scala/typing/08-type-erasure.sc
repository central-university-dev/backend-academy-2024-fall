val stringList: List[String] = List("one", "two", "three")
val intList: List[Int] = List(1, 2, 3)

stringList.getClass == intList.getClass // true

def convertToString[A](a: List[A]): List[String] = a match // will not work
  case a: List[Int] => a.map(_.toString)
  case a: List[String] => a
  case _ => Nil

convertToString(stringList)
convertToString(intList)

