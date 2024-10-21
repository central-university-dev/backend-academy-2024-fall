package tbank.petstore.repository.inmemory

import cats.Functor
import cats.syntax.functor.*
import tbank.petstore.repository.OrderRepository
import tbank.petstore.domain.order.Order
import java.util.UUID
import tbank.petstore.common.cache.Cache

class OrderRepositoryInMemory[F[_]: Functor](cache: Cache[F, UUID, Order])
    extends OrderRepository[F]:

  override def list: F[List[Order]] = cache.values

  override def get(id: UUID): F[Option[Order]] = cache.get(id)

  override def delete(id: UUID): F[Option[Order]] = cache.remove(id)

  override def create(order: Order): F[Long] = cache.add(order.id, order).as(1L)
end OrderRepositoryInMemory

object OrderRepositoryInMemory:
  def make[F[_]: Functor](cache: Cache[F, UUID, Order]) =
    new OrderRepositoryInMemory(cache)
