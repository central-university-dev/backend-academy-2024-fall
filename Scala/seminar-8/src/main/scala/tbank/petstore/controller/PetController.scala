package tbank.petstore.controller

import tbank.petstore.service.PetService
import tbank.petstore.common.controller.Controller
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.*
import java.util.UUID
import tbank.petstore.domain.pet.CreatePet
import tbank.petstore.domain.pet.PetResponse
import tbank.petstore.domain.pet.PetCategory

class PetController[F[_]](petService: PetService[F]) extends Controller[F]:
  val createPet: ServerEndpoint[Any, F] =
    endpoint.post
      .summary("Создать питомца")
      .in("api" / "v1" / "pet")
      .in(jsonBody[CreatePet])
      .out(jsonBody[PetResponse])
      .serverLogicSuccess(petService.create)

  val updatePet: ServerEndpoint[Any, F] =
    endpoint.patch
      .summary("Обновить питомца")
      .in("api" / "v1" / "pet" / path[UUID]("petId"))
      .in(jsonBody[CreatePet])
      .out(jsonBody[PetResponse])
      .serverLogicSuccess { case (id, createPet) =>
        petService.update(id, createPet)
      }

  val listPets: ServerEndpoint[Any, F] =
    endpoint.get
      .summary("Список питомцев")
      .in("api" / "v1" / "pet")
      .out(jsonBody[List[PetResponse]])
      .serverLogicSuccess(_ => petService.list)

  val getPet: ServerEndpoint[Any, F] =
    endpoint.get
      .summary("Получить питомца")
      .in("api" / "v1" / "pet" / path[UUID]("petId"))
      .out(jsonBody[Option[PetResponse]])
      .serverLogicSuccess(petService.get)

  val deletePet: ServerEndpoint[Any, F] =
    endpoint.delete
      .summary("Удалить питомца")
      .in("api" / "v1" / "pet" / path[UUID]("petId"))
      .out(jsonBody[Option[PetResponse]])
      .serverLogicSuccess(petService.delete)

  val listByCategoryPet: ServerEndpoint[Any, F] =
    endpoint.get
      .summary("Список питомцев по категории")
      .in("api" / "v1" / "pet" / "category" / path[PetCategory]("petCategory"))
      .out(jsonBody[List[PetResponse]])
      .serverLogicSuccess(petService.listByCategory)

  override val endpoints: List[ServerEndpoint[Any, F]] =
    List(createPet, updatePet, listPets, getPet, deletePet, listByCategoryPet)
      .map(_.withTag("Pet"))
end PetController

object PetController:
  def make[F[_]](petService: PetService[F]): PetController[F] =
    new PetController(petService)
end PetController
