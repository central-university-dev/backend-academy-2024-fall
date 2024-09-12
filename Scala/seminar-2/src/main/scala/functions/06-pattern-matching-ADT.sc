// Pattern matching
import scala.util.Random

val x: Int = Random.nextInt(10)


// syntax
x match
  case 0 => "zero"
  case 1 => "one"
  case 2 => "two"
  case _ => "other"

// pattern guard
x match
  case inner if inner > 5 => "big number"
  case _ => "low number"

// matching on type
val any: Any = "hello"

any match
  case s: String => s"string: $s"
  case i: Int => s"int: $i"
  case _ => "other"

// exhaustive matching
x match
  case 0 => "zero"
  case 1 => "one"
  case 2 => "two" // scala.MatchError

// ADT
sealed trait Piece
case object King extends Piece
case object Queen extends Piece
case object Rook extends Piece
case object Bishop extends Piece
case object Knight extends Piece
case object Pawn extends Piece

sealed trait Color
case object White extends Color
case object Black extends Color

case class ChessPiece(piece: Piece, color: Color)

case class Position(row: Int, col: Int)

case class ChessBoard(board: List[(Position, ChessPiece)])

val initialBoard = ChessBoard(List(
  Position(1, 1) -> ChessPiece(Rook, White),
  Position(1, 2) -> ChessPiece(Knight, White),
  Position(2, 1) -> ChessPiece(Pawn, White),
  Position(8, 8) -> ChessPiece(King, Black)
))

initialBoard.board.foreach {
  case (position, piece) => piece match
    case ChessPiece(King, color) => println(s"King of $color at $position")
    case ChessPiece(Queen, color) => println(s"Queen of $color at $position")
    case ChessPiece(_, Black) => println(s"Black piece at $position")
    case _ => println(s"Another piece at $position")
}
// Another piece at Position(1,1)
// Another piece at Position(1,2)
// Another piece at Position(2,1)
// King of Black at Position(8,8)

// Home tasks:
// 1. Write a function that returns a list of all positions of a given color
// 2. Write a function that returns a list of all positions of a given piece
// 3. White a function that will count White Knights on a board