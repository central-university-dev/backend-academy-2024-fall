package tbank.implicits

import exceptions.and.implicits.Implicit.Combine

object ImplicitTask {
  def combineList[T: Combine](list: List[T]): T = ???
  
//  @main
//  def main(): Unit = println(1)
}

object Implicit {
  trait Combine[T] {
    def combine(x: T, y: T): T
  }

  given Combine[String] = ???
  given multiplyIntCombine: Combine[Int] = ???
  given addIntCombine: Combine[Int] = ???
  given combineEither[L: Combine, R]: Combine[Either[L, R]] = ???
}
