package tbank.petstore.domain.pet

import tethys.JsonReader
import tethys.JsonObjectWriter
import sttp.tapir.Schema

case class CreatePet(
    name: String,
    category: PetCategory,
    description: String
) derives JsonReader,
      JsonObjectWriter,
      Schema
