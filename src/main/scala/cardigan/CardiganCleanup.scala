package cardigan

import cats.effect.{ IO, IOApp }
import fs2.io.file.{ Files, Path }
import fs2.{ Stream, text }

/** Make the input data from https://github.com/Henryjean/Rogers-Cardigans consistent. */
object CardiganCleanup extends IOApp.Simple {

  val converter: Stream[IO, Unit] = {
    def cleanup(line: String): String = {
      val dirtyCells = line.split(",")
      val dirtyColor = dirtyCells(2)
      val cleanColor = {
        val temp = dirtyColor.stripPrefix("#").toLowerCase
        if (temp == "na") "" else temp
      }
      val notes = dirtyCells.lift(3).getOrElse("")
      val cleanCells = dirtyCells.slice(0, 2) ++ Array(cleanColor, notes)
      cleanCells.mkString(",")
    }

    Files[IO].readAll(Path("./src/main/resources/cardigancolors.csv"))
      .through(text.utf8.decode)
      .through(text.lines)
      .filter(s => s.trim.nonEmpty)
      .map(line => cleanup(line))
      .intersperse("\n")
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(Path("./src/main/resources/clean_cardigancolors.csv")))
  }

  override def run: IO[Unit] = converter.compile.drain
}
