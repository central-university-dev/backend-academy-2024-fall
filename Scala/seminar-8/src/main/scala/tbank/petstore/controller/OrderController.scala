package tbank.petstore.controller

import tbank.petstore.service.OrderService
import tbank.petstore.common.controller.Controller
import tbank.petstore.domain.order.Order
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.*
import tbank.petstore.domain.order.CreateOrder
import tbank.petstore.domain.order.OrderResponse
import java.util.UUID

class OrderController[F[_]](orderService: OrderService[F])
    extends Controller[F]:
  val createOrder: ServerEndpoint[Any, F] =
    endpoint.post
      .summary("Создать заказ")
      .in("api" / "v1" / "order")
      .in(jsonBody[CreateOrder])
      .out(jsonBody[OrderResponse])
      .serverLogicSuccess(orderService.create)

  val listOrders: ServerEndpoint[Any, F] =
    endpoint.get
      .summary("Список заказов")
      .in("api" / "v1" / "order")
      .out(jsonBody[List[OrderResponse]])
      .serverLogicSuccess(_ => orderService.list)

  val getOrder: ServerEndpoint[Any, F] =
    endpoint.get
      .summary("Получить заказ")
      .in("api" / "v1" / "order" / path[UUID]("orderId"))
      .out(jsonBody[Option[OrderResponse]])
      .serverLogicSuccess(orderService.get)

  val deleteOrder: ServerEndpoint[Any, F] =
    endpoint.delete
      .summary("Удалить заказов")
      .in("api" / "v1" / "order" / path[UUID]("orderId"))
      .out(jsonBody[Option[OrderResponse]])
      .serverLogicSuccess(orderService.delete)

  override val endpoints: List[ServerEndpoint[Any, F]] =
    List(createOrder, listOrders, getOrder, deleteOrder)
      .map(_.withTag("Order"))
end OrderController

object OrderController:
  def make[F[_]](orderService: OrderService[F]): OrderController[F] =
    new OrderController[F](orderService)
end OrderController
