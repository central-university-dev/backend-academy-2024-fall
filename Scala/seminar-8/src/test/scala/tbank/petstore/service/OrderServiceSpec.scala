package tbank.petstore.service

import munit.CatsEffectSuite
import cats.syntax.option.*
import cats.syntax.functor.*
import cats.syntax.applicative.*
import cats.effect.kernel.Clock
import cats.Applicative
import scala.concurrent.duration.FiniteDuration
import cats.effect.kernel.Sync
import cats.effect.kernel.Ref
import cats.effect.IO
import scala.concurrent.duration.*
import tbank.petstore.repository.OrderRepository
import tbank.petstore.domain.order.Order
import java.util.UUID
import cats.effect.std.UUIDGen
import tbank.petstore.domain.order.CreateOrder
import tbank.petstore.service.OrderService.Impl
import tbank.petstore.domain.order.OrderResponse
import java.time.Instant
import tbank.petstore.common.cache.Cache
import tbank.petstore.repository.inmemory.OrderRepositoryInMemory

class OrderServiceSpec extends CatsEffectSuite {

  test(
    ".create should return created order response if success (without Clock and UUIDGen)"
  ) {
    val petId = UUID.randomUUID()
    val service: IO[OrderService[IO]] = for {
      cache <- Cache.make[IO, UUID, Order]
      repository = OrderRepositoryInMemory.make(cache)
    } yield OrderService.make[IO](repository)

    assertIO(
      service.flatMap(_.create(CreateOrder(petId))),
      OrderResponse(???, petId, ???)
    )
  }

  test(".create should return created order response if success") {
    val orderId = UUID
      .fromString("123e4567-e89b-12d3-a456-426655440000")
    val petId = UUID.randomUUID()
    // init
    val service: IO[OrderService[IO]] = for {
      given Clock[IO] <- TestClock.make[IO](0.second)
      given UUIDGen[IO] = new UUIDGen[IO] {
        override def randomUUID: IO[UUID] = orderId.pure[IO]
      }
      cache <- Cache.make[IO, UUID, Order]
      repository = OrderRepositoryInMemory.make(cache)
    } yield OrderService.make[IO](repository)

    assertIO(
      service.flatMap(_.create(CreateOrder(petId))),
      OrderResponse(orderId, petId, Instant.ofEpochMilli(0))
    )
  }

  object TestClock:
    def make[F[_]: Clock: Sync](startTime: FiniteDuration) = Ref
      .in[F, F, FiniteDuration](startTime)
      .map(clock =>
        new Clock[F] {

          override def applicative: Applicative[F] = Applicative[F]

          override def monotonic: F[FiniteDuration] =
            clock.getAndUpdate(_ + 1.second)

          override def realTime: F[FiniteDuration] =
            clock.getAndUpdate(_ + 1.second)
        }
      )
}
