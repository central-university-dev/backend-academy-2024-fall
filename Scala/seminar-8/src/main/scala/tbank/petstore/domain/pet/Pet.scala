package tbank.petstore.domain.pet

import java.util.UUID

case class Pet(
    id: UUID,
    name: String,
    category: PetCategory,
    description: String
) {
  def toResponse: PetResponse =
    PetResponse(
      id = id,
      name = name,
      category = category,
      description = description
    )
}

object Pet {
  def fromCreatePet(id: UUID, createPet: CreatePet): Pet =
    Pet(
      id = id,
      name = createPet.name,
      category = createPet.category,
      description = createPet.description
    )
}
