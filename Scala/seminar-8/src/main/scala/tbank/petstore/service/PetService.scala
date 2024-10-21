package tbank.petstore.service

import tbank.petstore.domain.pet.PetResponse
import java.util.UUID
import tbank.petstore.domain.pet.CreatePet
import tbank.petstore.domain.pet.PetCategory
import cats.FlatMap
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import tbank.petstore.repository.PetRepository
import cats.effect.std.UUIDGen
import tbank.petstore.domain.pet.Pet

trait PetService[F[_]]:
  def create(createPet: CreatePet): F[PetResponse]

  def update(id: UUID, createPet: CreatePet): F[PetResponse]

  def list: F[List[PetResponse]]

  def get(id: UUID): F[Option[PetResponse]]

  def delete(id: UUID): F[Option[PetResponse]]

  def listByCategory(category: PetCategory): F[List[PetResponse]]

object PetService:
  case class Impl[F[_]: UUIDGen: FlatMap](petRepository: PetRepository[F])
      extends PetService[F]:
    override def create(createPet: CreatePet): F[PetResponse] =
      for {
        id <- UUIDGen[F].randomUUID
        pet = Pet.fromCreatePet(id, createPet)
        _ <- petRepository.create(pet)
      } yield pet.toResponse

    override def update(id: UUID, createPet: CreatePet): F[PetResponse] =
      petRepository.update(Pet.fromCreatePet(id, createPet)).map(_.toResponse)

    override def list: F[List[PetResponse]] =
      petRepository.list.map(_.map(_.toResponse))

    override def get(id: UUID): F[Option[PetResponse]] =
      petRepository.get(id).map(_.map(_.toResponse))

    override def delete(id: UUID): F[Option[PetResponse]] =
      petRepository.delete(id).map(_.map(_.toResponse))

    override def listByCategory(category: PetCategory): F[List[PetResponse]] =
      petRepository.listByCategory(category).map(_.map(_.toResponse))
  end Impl

  def make[F[_]: UUIDGen: FlatMap](petRepository: PetRepository[F]) = new Impl(
    petRepository
  )
end PetService
