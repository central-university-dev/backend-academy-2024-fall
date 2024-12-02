package tbank.sample.project

import cats.data.ReaderT
import cats.effect.*
import cats.effect.std.Console
import cats.syntax.all.*
import cats.syntax.flatMap.*

import java.io.{IO => _, *}
import java.nio.file.Files

object Task2Fixed extends IOApp {
  private case class FilesConfig(
    originFile: String = "seminar-13/src/main/resources/in.txt",
    destFile: String = "seminar-13/src/main/resources/out.txt"
  )
  private type FilesIO[A] = ReaderT[IO, FilesConfig, A]

  private def validateFiles[F[_]: Sync: Console](
    origin: File,
    destination: File
  ): F[Unit] =
    for {
      _ <- if (origin.getCanonicalPath == destination.getCanonicalPath) {
             Sync[F].raiseError(
               new IOException(
                 "Origin and destination files cannot be the same."
               )
             )
           } else Sync[F].unit

      _ <- if (!origin.canRead) {
             Sync[F].raiseError(
               new IOException(
                 s"Cannot read the origin file: ${origin.getPath}"
               )
             )
           } else Sync[F].unit

      _ <- if (!destination.canWrite) {
             Sync[F].raiseError(
               new IOException(
                 s"Cannot write to the destination file: ${destination.getPath}"
               )
             )
           } else Sync[F].unit

      _ <- if (destination.exists()) {
             for {
               _ <-
                 Console[F].println(
                   s"The destination file ${destination.getPath} already exists. Overwrite? (y/n)"
                 )
               response <- Console[F].readLine
               _ <- if (response.trim.toLowerCase != "y") {
                      Sync[F].raiseError(
                        new IOException("Operation aborted by the user.")
                      )
                    } else Sync[F].unit
             } yield ()
           } else Sync[F].unit
    } yield ()

  private def copy[F[_]: Sync: Console](
    origin: File,
    destination: File,
    bufferSize: Int = 1024 * 10
  ): F[Long] =
    inputOutputStreams(origin, destination).use { case (in, out) =>
      transfer(in, out, new Array[Byte](bufferSize), 0L)
    }

  private def inputOutputStreams[F[_]: Sync](
    in: File,
    out: File
  ): Resource[F, (InputStream, OutputStream)] =
    for {
      inStream  <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  private def transfer[F[_]: Sync: Console](
    origin: InputStream,
    destination: OutputStream,
    buffer: Array[Byte],
    acc: Long
  ): F[Long] =
    for {
      amount <- Sync[F].blocking(origin.read(buffer, 0, buffer.length))
      count <- if (amount > -1) {
                 Sync[F].blocking(
                   destination.write(buffer, 0, amount)
                 ) >> transfer(
                   origin,
                   destination,
                   buffer,
                   acc + amount
                 )
               } else {
                 Sync[F].pure(acc)
               }
    } yield count

  private def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].blocking(new FileInputStream(f))
    } { inStream =>
      Sync[F].blocking(inStream.close()).handleErrorWith(_ => Sync[F].unit)
    }

  private def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] =
    Resource.make {
      Sync[F].blocking(new FileOutputStream(f))
    } { outStream =>
      Sync[F].blocking(outStream.close()).handleErrorWith(_ => Sync[F].unit)
    }

  private def copyDirectory[F[_]: Sync: Console](
    source: File,
    destination: File
  ): F[Unit] = for {
    _     <- Sync[F].blocking(Files.createDirectories(destination.toPath))
    files <- Sync[F].blocking(source.listFiles())
    _ <- files.toList.traverse { file =>
           val newDest = new File(destination, file.getName)
           if (file.isDirectory) {
             copyDirectory(file, newDest)
           } else {
             copy[F](file, newDest).void
           }
         }
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      config <- ReaderT.ask[IO, FilesConfig]
      orig = new File(config.originFile)
      dest = new File(config.destFile)
      _ <- validateFiles[FilesIO](orig, dest)
      _ <- if (orig.isDirectory) {
             copyDirectory[FilesIO](orig, dest)
           } else {
             copy[FilesIO](orig, dest)
           }
    } yield ExitCode.Success)
      .run(FilesConfig())
}
