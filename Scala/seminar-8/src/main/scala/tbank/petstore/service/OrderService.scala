package tbank.petstore.service

import tbank.petstore.domain.order.CreateOrder
import tbank.petstore.domain.order.OrderResponse
import java.util.UUID
import cats.effect.std.UUIDGen
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.FlatMap
import cats.effect.kernel.Clock
import tbank.petstore.repository.OrderRepository
import tbank.petstore.domain.order.Order
import cats.instances.order
import cats.Id

trait OrderService[F[_]]:
  def create(createOrder: CreateOrder): F[OrderResponse]

  def list: F[List[OrderResponse]]

  def get(id: UUID): F[Option[OrderResponse]]

  def delete(id: UUID): F[Option[OrderResponse]]
end OrderService

object OrderService:
  case class Impl[F[_]: UUIDGen: FlatMap: Clock](
      orderRepository: OrderRepository[F]
  ) extends OrderService[F]:
    override def create(createOrder: CreateOrder): F[OrderResponse] =
      for {
        id <- UUIDGen[F].randomUUID
        now <- Clock[F].realTimeInstant
        order = Order.fromCreateOrder(id, now, createOrder)
        _ <- orderRepository.create(order)
      } yield order.toResponse

    override def list: F[List[OrderResponse]] =
      orderRepository.list.map(_.map(_.toResponse))

    override def get(id: UUID): F[Option[OrderResponse]] =
      orderRepository.get(id).map(_.map(_.toResponse))

    override def delete(id: UUID): F[Option[OrderResponse]] =
      orderRepository.delete(id).map(_.map(_.toResponse))
  end Impl

  def make[F[_]: UUIDGen: FlatMap: Clock](orderRepository: OrderRepository[F]) =
    new Impl(orderRepository)
end OrderService
