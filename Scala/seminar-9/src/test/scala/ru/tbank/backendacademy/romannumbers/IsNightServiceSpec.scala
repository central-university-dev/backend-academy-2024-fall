package ru.tbank.backendacademy.romannumbers

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.{Clock, Instant, ZoneId}

class IsNightServiceSpec extends AnyFlatSpec with Matchers with MockFactory {

  "IsNightService" should "report day" in {
    val clockMock = mock[Clock]
    (clockMock.instant _).expects().twice.returns(Instant.parse("2007-12-03T15:15:30.00Z"))
    (clockMock.getZone _).expects().twice.returns(ZoneId.of("Z"))
    new IsNightService(clockMock)
      .isNight() shouldEqual false
  }

  "IsNightService" should "report night" in {
    new IsNightService(Clock.fixed(Instant.parse("2007-12-03T05:15:30.00Z"), ZoneId.of("Z")))
      .isNight() shouldEqual true
  }

}
