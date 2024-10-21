package tbank.petstore.domain.pet

import tethys.StringEnumJsonWriter
import tethys.StringEnumJsonReader
import sttp.tapir.Schema
import sttp.tapir.Codec
import sttp.tapir.Codec.PlainCodec

enum PetCategory derives StringEnumJsonWriter, StringEnumJsonReader:
  case Snake, Amphibian, Mammal, Reptile, Bird, Fish, Worm

object PetCategory:
  given Schema[PetCategory] =
    Schema.derivedEnumeration[PetCategory].defaultStringBased

  given PlainCodec[PetCategory] =
    Codec.derivedEnumeration[String, PetCategory].defaultStringBased
end PetCategory
