// in-box ADTs:
// Option
val none: Option[Int] = None
val some: Option[Int] = Some(1)

def divideOpt(a: Int, b: Int): Option[Int] =
  if (b == 0) None else Some(a / b)

divideOpt(100, 0) match
  case Some(value) => println(s"Result: $value")
  case None => println("Error: Division by zero")

// Either
val left: Either[String, Int] = Left("Error")
val right: Either[String, Int] = Right(1)

def divideEither(a: Int, b: Int): Either[String, Int] =
  if (b == 0) Left("Division by zero") else Right(a / b)

divideEither(100, 0) match
  case Right(value) => println(s"Result: $value")
  case Left(error) => println(s"Error: $error")

// Home tasks:
// 1. Create a function `findElement` that takes a list of integers and an element to find. If the element is present in the list, return it wrapped in `Some`, otherwise return `None`.
// 2. Write a function `stringToInt` that converts a string to an integer. Return `Some` with the integer value if the conversion is successful, otherwise return `None`.
// 3. Create a method `validateAge` that takes an age as an integer and validates if the age is between 18 and 65. Return `Right` with the valid age if it passes the validation, otherwise return `Left` with an error message.
// 4. Define a function `concatenateStrings` that takes two strings and concatenates them. Return an `Either` with the concatenated string if both inputs are non-empty, otherwise return an error message as `Left`.
// 5. Test the above functions with different inputs to check the behavior of `Option` and `Either`, handling both successful cases and error scenarios.