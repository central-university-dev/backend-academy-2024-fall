package tbank.petstore.repository

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tbank.petstore.repository
import java.util.UUID
import java.time.Instant
import tbank.petstore.domain.order.Order
import cats.effect.SyncIO
import munit.CatsEffectSuite
import cats.effect.IO

trait OrderRepositoryBehaviors extends munit.FunSuite {
  this: CatsEffectSuite =>

  def validWordRepository(
      testedRepository: => IO[OrderRepository[IO]]
  )(implicit loc: munit.Location): Unit = {

    val repository: IO[OrderRepository[IO]] = testedRepository

    test("return no word if empty") {
      assertIO(
        repository.flatMap(_.get(UUID.fromString("7A5D6558-7FFC-4FCA-AF8A-D9ACC8BDE6ED"))),
        None
      )
    }

    test("return added word") {
      val orderId = UUID.fromString("7A5D6558-7FFC-4FCA-AF8A-D9ACC8BDE6ED")
      val order = Order(
        orderId,
        UUID.fromString("E83C54E5-B81F-4999-A1FA-4FC4CCC368BB"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )

      assertIO(
        repository.flatMap(r => r.create(order) >> r.get(orderId)),
        Some(order)
      )
    }

    test("repository overwrites orders by key") {
      val order1 = Order(
        UUID.fromString("7A5D6558-7FFC-4FCA-AF8A-D9ACC8BDE6ED"),
        UUID.fromString("E83C54E5-B81F-4999-A1FA-4FC4CCC368BB"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )
      val order2 = Order(
        UUID.fromString("7A5D6558-7FFC-4FCA-AF8A-D9ACC8BDE6ED"),
        UUID.fromString("D90BEDE4-B00B-44AA-B5FD-295BC17F5F43"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )
      val order3 = Order(
        UUID.fromString("7A5D6558-7FFC-4FCA-AF8A-D9ACC8BDE6ED"),
        UUID.fromString("3F271CB6-F157-40E9-BD74-132A86FC5152"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )

      val list: IO[List[Order]] = repository.flatMap(r =>
        for {
          _ <- r.create(order1)
          _ <- r.create(order2)
          _ <- r.create(order3)
          l <- r.list
        } yield l
      )

      assertIOBoolean(list.map { l =>
          !l.contains(order1) && !l.contains(order2) && l.contains(order3)
      })
    }

    test("list returns orders, added with multiple creates") {
      val order1 = Order(
        UUID.fromString("7A5D6558-7FFC-4FCA-AF8A-D9ACC8BDE6ED"),
        UUID.fromString("E83C54E5-B81F-4999-A1FA-4FC4CCC368BB"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )
      val order2 = Order(
        UUID.fromString("9FF249FF-9119-48E4-8C77-1666630F6ED0"),
        UUID.fromString("E83C54E5-B81F-4999-A1FA-4FC4CCC368BB"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )
      val order3 = Order(
        UUID.fromString("70554CBC-C12E-455F-846C-1A6AE0BC31E2"),
        UUID.fromString("E83C54E5-B81F-4999-A1FA-4FC4CCC368BB"),
        Instant.parse("2007-12-03T10:15:30.00Z")
      )

      val list: IO[List[Order]] = repository.flatMap(r =>
        for {
          _ <- r.create(order1)
          _ <- r.create(order2)
          _ <- r.create(order3)
          l <- r.list
        } yield l
      )

      assertIOBoolean(list.map { l =>
          l.contains(order1) && l.contains(order2) && l.contains(order3)
      })
    }

  }

}
