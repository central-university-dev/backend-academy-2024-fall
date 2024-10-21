package tbank.petstore.domain.order

import tethys.JsonObjectWriter
import tethys.JsonReader
import java.time.Instant
import java.util.UUID
import sttp.tapir.Schema

case class OrderResponse(
    id: UUID,
    petId: UUID,
    date: Instant
) derives JsonReader,
      JsonObjectWriter,
      Schema
