import scala.util.Random

enum Suit:
  case Hearts, Diamonds, Clubs, Spades

enum Rank:
  case Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King

case class Card[S <: Suit, R <: Rank](suit: S, rank: R)

case class Hand[A <: Card[_, _]](cards: List[A]) extends AnyVal:
  def addCard(card: A): Hand[A] = Hand(cards :+ card)

object BlackJack:
  val scoreMap: Map[Rank, Int] = Map(
    Rank.Ace -> 11,
    Rank.Two -> 2,
    Rank.Three -> 3,
    Rank.Four -> 4,
    Rank.Five -> 5,
    Rank.Six -> 6,
    Rank.Seven -> 7,
    Rank.Eight -> 8,
    Rank.Nine -> 9,
    Rank.Ten -> 10,
    Rank.Jack -> 10,
    Rank.Queen -> 10,
    Rank.King -> 10
  )

  def calculateScore(hand: Hand[Card[Suit, Rank]]): Int = hand.cards.foldLeft(0) { (acc, card) =>
    acc + scoreMap(card.rank)
  }

val allCards: List[Card[Suit, Rank]] = for {
  suite <- Suit.values
  rank <- Rank.values
} yield Card(suite, rank)

// extra tasks

case class Deck[S <: Suit, R <: Rank](cards: List[Card[S, R]]) extends AnyVal:
  def drawCard: Option[(Card[S, R], Deck[S, R])] = cards.headOption.map(card => (card, Deck(cards.tail)))
  def shuffle: Deck[S, R] = Deck(Random.shuffle(cards))

object Deck:
  def default: Deck[Suit, Rank] = Deck(Suit.values.flatMap(suit => Rank.values.map(rank => Card(suit, rank))).toList)

val deck = Deck.default.shuffle

val state: Option[(Hand[Card[Suit, Rank]], Deck[Suit, Rank])] = for {
  (card1, deck1) <- deck.drawCard
  (card2, deck2) <- deck1.drawCard
} yield (Hand(List(card1, card2)), deck2)

val score = state.map { (hand, _) => BlackJack.calculateScore(hand) }
