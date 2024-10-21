package tbank.petstore.domain.order

import java.time.Instant
import java.util.UUID

case class Order(
    id: UUID,
    petId: UUID,
    date: Instant
) {
  def toResponse: OrderResponse =
    OrderResponse(
      id = id,
      petId = petId,
      date = date
    )
}

object Order {
  def fromCreateOrder(
      id: UUID,
      date: Instant,
      createOrder: CreateOrder
  ): Order =
    Order(
      id = id,
      petId = createOrder.petId,
      date = date
    )
}
