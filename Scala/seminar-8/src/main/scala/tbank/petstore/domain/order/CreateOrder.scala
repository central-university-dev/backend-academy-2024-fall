package tbank.petstore.domain.order

import sttp.tapir.Schema
import tethys.*
import tethys.jackson.*
import tethys.{JsonReader, JsonWriter}

import java.time.Instant
import java.util.UUID
import tethys.JsonObjectWriter

case class CreateOrder(petId: UUID) derives JsonReader, JsonObjectWriter, Schema
