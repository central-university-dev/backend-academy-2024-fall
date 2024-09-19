final case class UserId(inner: String)
final case class PaymentId(inner: String)

val users =
  Map(UserId("1") -> "Grisha", UserId("2") -> "Vasya", UserId("3") -> "Petya")

def deleteUser(userId: UserId): (Map[UserId, String], Option[String]) =
  (users.removed(userId), users.get(userId))

deleteUser(UserId("1")) // ok
deleteUser(PaymentId("12")) // not ok
