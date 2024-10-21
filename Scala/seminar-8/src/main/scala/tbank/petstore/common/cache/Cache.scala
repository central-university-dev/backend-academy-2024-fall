package tbank.petstore.common.cache

import cats.effect.Sync

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap

trait Cache[F[_], K, V] {
  def add(key: K, value: V): F[Unit]
  def values: F[List[V]]
  def get(key: K): F[Option[V]]
  def remove(key: K): F[Option[V]]
  def update(key: K, value: V): F[Unit]
}

object Cache {
  private class InMemory[F[_]: Sync, K, V](map: concurrent.Map[K, V]) extends Cache[F, K, V] {
    override def add(key: K, value: V): F[Unit] =
      Sync[F]
        .delay(map += key -> value)

    override def values: F[List[V]] =
      Sync[F].delay(map.values.toList)

    override def get(key: K): F[Option[V]] =
      Sync[F].delay(map.get(key))

    override def remove(key: K): F[Option[V]] =
      Sync[F].delay(map.remove(key))

    override def update(key: K, value: V): F[Unit] =
      Sync[F].delay(map.update(key, value))
  }

  // Аллоцируем изменяемое состояние, поэтому без F снаружи будет не RT
  def make[F[_]: Sync, K, V]: F[Cache[F, K, V]] =
    Sync[F].delay(new InMemory[F, K, V](new TrieMap[K, V]))
}
