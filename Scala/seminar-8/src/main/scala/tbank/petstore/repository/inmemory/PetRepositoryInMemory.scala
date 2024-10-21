package tbank.petstore.repository.inmemory

import cats.Functor
import cats.syntax.functor.*
import tbank.petstore.common.cache.Cache
import tbank.petstore.domain.pet.Pet
import java.util.UUID
import tbank.petstore.domain.pet.PetCategory
import tbank.petstore.repository.PetRepository

class PetRepositoryInMemory[F[_]: Functor](cache: Cache[F, UUID, Pet])
    extends PetRepository[F]:

  override def create(pet: Pet): F[Long] = cache.add(pet.id, pet).as(1L)

  override def update(pet: Pet): F[Pet] = cache.update(pet.id, pet).as(pet)

  override def list: F[List[Pet]] = cache.values

  override def get(id: UUID): F[Option[Pet]] = cache.get(id)

  override def delete(id: UUID): F[Option[Pet]] = cache.remove(id)

  override def listByCategory(category: PetCategory): F[List[Pet]] =
    cache.values.map(_.filter(_.category == category))

end PetRepositoryInMemory

object PetRepositoryInMemory:
  def make[F[_]: Functor](cache: Cache[F, UUID, Pet]) =
    new PetRepositoryInMemory(cache)
