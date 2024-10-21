package tbank.petstore.domain.pet

import java.util.UUID
import tethys.JsonReader
import tethys.JsonObjectWriter
import sttp.tapir.Schema

case class PetResponse(
    id: UUID,
    name: String,
    category: PetCategory,
    description: String
) derives JsonReader,
      JsonObjectWriter,
      Schema
