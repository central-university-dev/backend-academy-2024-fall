package typing

object OpaqueTypes extends App {
  opaque type UserId = String
  object UserId {
    def apply(value: String): UserId = value
  }
  opaque type PaymentId = String
  object PaymentId {
    def apply(value: String): PaymentId = value
  }

  val users: Map[UserId, String] =
    Map(UserId("1") -> "Grisha", UserId("2") -> "Vasya", UserId("3") -> "Petya")

  def deleteUser(userId: UserId): (Map[UserId, String], Option[String]) =
    (users.removed(userId), users.get(userId))

  val (result, deleted) = deleteUser(UserId("1"))
  println(result)
  println(deleted)
}
