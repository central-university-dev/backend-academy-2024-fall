class Box[T](inner: T)

val intBox: Box[Int] = Box(123)
val stringBox: Box[String] = Box("one hundred")

// type erasure
def whatIsTheBox[A](box: Box[A]): Unit = // error
  box match
    case a: Box[Int] => println("this is int")
    case a: Box[String] => println("this is string")
    case _ => println("idk what is this box")

whatIsTheBox(intBox)

intBox.getClass == stringBox.getClass // true
