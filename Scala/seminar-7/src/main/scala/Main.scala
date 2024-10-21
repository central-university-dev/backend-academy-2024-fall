package ru.tbank

import cats.effect.{IO, IOApp, Resource}
import cats.syntax.traverse.*
import cats.syntax.foldable.*

import scala.io.Source
import scala.util.control.NoStackTrace

/*
   Задача:

   Файл students.txt содержит список студентов с несколькими оценками,
   каждая строка имеет формат "Фамилия:баллы"

   Фамилия пишется латинскими буквами, баллы - целое число от 1 до 10.

   Требуется напечатать средний балл по фамилии
 */

object Main extends IOApp.Simple:

  override def run: IO[Unit] =
    Resource.fromAutoCloseable(
      IO(Source.fromResource("students.txt"))
    ).use { file =>
      val lines = file.getLines().toList

      for {
        studentMarks <- lines.zipWithIndex.traverse(parseStudentMark)
        averageMark = studentMarks.groupMap(_.student)(_.mark).view.mapValues(marks =>
          marks.sum.toDouble / marks.size
        ).toList
        _ <- averageMark.traverse_ { case (student, averageMark) =>
          IO.println(f"Student: $student, average mark: $averageMark%.2f")
        }
      } yield ()
    }.recoverWith {
      case FormatException(msg, lineNumber) => IO.println(s"Error in line: $lineNumber: $msg")
      case e: Throwable => IO.println(s"Unknown error: ${e.getMessage}")
    }

  private def parseStudentMark(line: String, lineNumber: Int): IO[StudentMark] =
    line.split(':').toList match
      case student :: markStr :: Nil =>
        IO.fromOption[Int](
          markStr.toIntOption.filter(v => v >= 1 && v <= 10)
        )(
          orElse = FormatException(s"Expected a number from 1 to 10, got \"$markStr\"", lineNumber)
        ).map(
          mark => StudentMark(student, mark)
        )
      case _ => IO.raiseError(FormatException(s"Invalid format, got \"$line\"", lineNumber))

  case class StudentMark(student: String, mark: Int)

  case class FormatException(message: String, lineNumber: Int) extends Exception(message) with NoStackTrace
