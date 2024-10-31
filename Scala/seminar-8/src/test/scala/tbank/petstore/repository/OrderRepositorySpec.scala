package tbank.petstore.repository

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tbank.petstore.repository.OrderRepository

import java.nio.file.Files
import tbank.petstore.repository.inmemory.OrderRepositoryInMemory
import cats.effect.SyncIO
import cats.Id
import tbank.petstore.common.cache.Cache
import java.util.UUID
import tbank.petstore.domain.order.Order
import munit.CatsEffectSuite
import cats.effect.IO

class OrderRepositorySpec
    extends CatsEffectSuite
    with OrderRepositoryBehaviors {

  validWordRepository(
    Cache.make[IO, UUID, Order].map(c => new OrderRepositoryInMemory[IO](c))
  )

}
