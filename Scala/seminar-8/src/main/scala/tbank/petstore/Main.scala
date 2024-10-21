package tbank.petstore

import cats.MonadThrow
import cats.data.OptionT
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Env
import cats.syntax.option.*
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import tbank.petstore.common.cache.Cache
import tbank.petstore.controller.OrderController
import tbank.petstore.controller.PetController
import tbank.petstore.domain.order.Order
import tbank.petstore.domain.pet.Pet
import tbank.petstore.repository.inmemory.OrderRepositoryInMemory
import tbank.petstore.repository.inmemory.PetRepositoryInMemory
import tbank.petstore.service.OrderService
import tbank.petstore.service.PetService

import java.util.UUID

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    for {
      orderCache <- Cache.make[IO, UUID, Order]
      petCache <- Cache.make[IO, UUID, Pet]
      endpoints <- IO.delay {
        List(
          OrderController.make(
            OrderService.make(OrderRepositoryInMemory.make(orderCache))
          ),
          PetController.make(
            PetService.make(PetRepositoryInMemory.make(petCache))
          )
        ).flatMap(_.endpoints)
      }
      swagger = SwaggerInterpreter()
        .fromServerEndpoints[IO](endpoints, "pet-store", "1.0.0")
      routes = Http4sServerInterpreter[IO]()
        .toRoutes(swagger ++ endpoints)
      port <- getPort[IO]
      _ <- EmberServerBuilder
        .default[IO]
        .withHost(Host.fromString("localhost").get)
        .withPort(port)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .build
        .use { server =>
          for {
            _ <- IO.println(
              s"Go to http://localhost:${server.address.getPort}/docs to open SwaggerUI. Press ENTER key to exit."
            )
            _ <- IO.readLine
          } yield ()
        }
    } yield ExitCode.Success

  private def getPort[F[_]: Env: MonadThrow]: F[Port] = {
    OptionT(Env[F].get("HTTP_PORT"))
      .toRight("HTTP_PORT not found")
      .subflatMap((ps: String) =>
        ps.toIntOption.toRight(
          s"Expected int in HTTP_PORT env variable, but got $ps"
        )
      )
      .subflatMap(pi => Port.fromInt(pi).toRight(s"No such port $pi"))
      .leftMap(new IllegalArgumentException(_))
      .rethrowT
  }
end Main
