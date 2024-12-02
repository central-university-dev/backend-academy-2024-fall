package tbank.sample.project

import cats.data.ReaderT
import cats.effect.*
import cats.syntax.all.*

import java.io.{IO => _, *}
import scala.util.matching.Regex

/*
Работа с cats-effect и коллекциями.

Задание:
- Читаем логи из одного файла, записываем в другой
- Путь до файла берем из конфига из ReaderT
- Считаем статистику по уровням логирования, данных логов
- Записываем в новый файл статистику в формате:
TotalLogs = $count
InfoLogs = $count
WarnLogs = $count
ErrorLogs = $count
WithoutLogs = $count
 */

object Task2 extends IOApp {
  private case class FilesConfig(
    originFile: String = "seminar-13/src/main/resources/in.txt",
    destFile: String = "seminar-13/src/main/resources/out.txt"
  )
  private type FilesIO[A] = ReaderT[IO, FilesConfig, A]

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      config <- ReaderT.ask[IO, FilesConfig]
      orig = new File(config.originFile)
      dest = new File(config.destFile)
      count <- copy[FilesIO](orig, dest)
      _ <- std.Console[FilesIO].println(
             s"$count bytes copied from ${orig.getPath} to ${dest.getPath}"
           )
    } yield ExitCode.Success)
      .run(FilesConfig())

  private def copy[F[_]: Sync](origin: File, destination: File): F[Long] =
    inputOutputStreams(origin, destination).use { case (in, out) =>
      transfer(in, out, new Array[Byte](1024 * 10), 0L)
    }

  private def inputOutputStreams[F[_]: Sync](
    in: File,
    out: File
  ): Resource[F, (InputStream, OutputStream)] =
    for {
      inStream  <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  private def transfer[F[_]: Sync](
    origin: InputStream,
    destination: OutputStream,
    buffer: Array[Byte],
    acc: Long
  ): F[Long] = for {
    amount <- Sync[F].blocking(origin.read(buffer, 0, buffer.length))
    s = new String(buffer)
    count <- if (amount > -1)
               Sync[F].blocking(
                 destination.write(buffer, 0, amount)
               ) >> transfer(origin, destination, buffer, acc + amount)
             else
               Sync[F].pure(acc) // End of read stream reached
  } yield count // Returns the actual amount of bytes transferred

  private def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].blocking(new FileInputStream(f))
    } { inStream =>
      Sync[F].blocking(inStream.close())
        .handleErrorWith(_ => Sync[F].unit)
    }

  private def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] =
    Resource.make {
      Sync[F].blocking(new FileOutputStream(f))
    } { outStream =>
      Sync[F].blocking(outStream.close())
        .handleErrorWith(_ => Sync[F].unit)
    }

  private case class Log(level: LogLevel, message: String)

  private object Log {
    def parse(s: String): Log = s match {
      case LevelRegex(level, message) => Log(LogLevel.valueOf(level), message)
      case message                    => Log(LogLevel.Without, message)
    }

    private val LevelRegex: Regex = """\[(Info|Warn|Error)] (.+)""".r
  }

  private enum LogLevel {
    case Info, Warn, Error, Without
  }
}
