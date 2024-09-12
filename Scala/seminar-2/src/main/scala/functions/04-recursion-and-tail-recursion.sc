// Recursion

val numbers = List(10, 5, -2, 16, 4)

// get a sum of numbers in a list
def sum(numbers: List[Int]): Int =
  var result = 0
  for (number <- numbers) result += number
  result

// recursive method
def sumRec(numbers: List[Int]): Int =
  if numbers.isEmpty then 0
  else numbers.head + sumRec(numbers.tail)

// tail recursive method
import scala.annotation.tailrec

def sumTailRec(numbers: List[Int]): Int =
  @tailrec
  def loop(numbers: List[Int], result: Int): Int =
    if numbers.isEmpty then result
    else loop(numbers.tail, result + numbers.head)

// Home tasks:
// 1. Implement method that calculates factorial of a number using tail recursion
// 2. Implement method that calculates fibonacci number using tail recursion