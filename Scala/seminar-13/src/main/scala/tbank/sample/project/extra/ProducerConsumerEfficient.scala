package tbank.sample.project.extra

import cats.Applicative
import cats.effect.*
import cats.effect.std.Console
import cats.instances.list.*
import cats.syntax.all.*

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

object ProducerConsumerEfficient extends IOApp {

  case class State[F[_], A](queue: Queue[A], takers: Queue[Deferred[F, A]])

  private object State {
    def empty[F[_], A]: State[F, A] = State(Queue.empty, Queue.empty)
  }

  def producer[F[_]: Temporal: Console](
    id: Int,
    counterR: Ref[F, Int],
    stateR: Ref[F, State[F, Int]]
  ): F[Unit] = {

    def offer(i: Int): F[Unit] =
      stateR.modify {
        case State(queue, takers) if takers.nonEmpty =>
          val (taker, rest) = takers.dequeue
          State(queue, rest) -> taker.complete(i).void
        case State(queue, takers) =>
          State(queue.enqueue(i), takers) -> Applicative[F].unit
      }.flatten

    for {
      i <- counterR.getAndUpdate(_ + 1)
      _ <- offer(i)
      _ <- Applicative[F].whenA(i % 100000 == 0)(
             Console[F].println(s"Producer $id has reached $i items")
           )
      _ <- Temporal[F].sleep(1.microsecond) // To prevent overwhelming consumers
      _ <- producer(id, counterR, stateR)
    } yield ()
  }

  def consumer[F[_]: Concurrent: Console](
    id: Int,
    stateR: Ref[F, State[F, Int]]
  ): F[Unit] = {

    val take: F[Int] =
      Deferred[F, Int].flatMap { taker =>
        stateR.modify {
          case State(queue, takers) if queue.nonEmpty =>
            val (i, rest) = queue.dequeue
            State(rest, takers) -> Applicative[F].pure(i)
          case State(queue, takers) =>
            State(queue, takers.enqueue(taker)) -> taker.get
        }.flatten
      }

    for {
      i <- take
      _ <- Applicative[F].whenA(i % 10000 == 0)(
             Console[F].println(s"Consumer $id has reached $i items")
           )
      _ <- consumer(id, stateR)
    } yield ()
  }

  override def run(args: List[String]): IO[ExitCode] =
    for {
      stateR   <- Ref.of[IO, State[IO, Int]](State.empty[IO, Int])
      counterR <- Ref.of[IO, Int](1)
      producers =
        List.range(1, 11).map(producer(_, counterR, stateR)) // 10 producers
      consumers = List.range(1, 11).map(consumer(_, stateR)) // 10 consumers
      res <-
        (producers ++ consumers)
          .parSequence.as(
            ExitCode.Success
          ) // Run producers and consumers in parallel until done (likely by user cancelling with CTRL-C)
          .handleErrorWith { t =>
            Console[IO].errorln(s"Error caught: ${t.getMessage}").as(
              ExitCode.Error
            )
          }
    } yield res
}
