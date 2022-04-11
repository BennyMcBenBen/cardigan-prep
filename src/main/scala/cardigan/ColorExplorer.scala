package cardigan

import cats.effect.{ IO, IOApp }

import scala.io.Source

object ColorExplorer extends IOApp.Simple {
  private val readColors = IO {
    val ignore = Set("colorcode", "")
    val input = Source.fromFile("./src/main/resources/clean_cardigancolors.csv")
    val colors = input.getLines()
      .filter(_.nonEmpty)
      .map(line => line.split(","))
      .filter(_.length > 2)
      .map(_ (2))
      .filterNot(ignore.contains)
      .toList
    input.close()

    val colorMap = colors.groupBy(identity).view.mapValues(_.size)
    println(s"Mr. Rogers wore ${colorMap.size} different color cardigans")

    val colorsDesc = colorMap.toList.sortBy(-_._2)
    colorsDesc.foreach(println)

    println(colorMap.keys.toList.sorted.map(c => s"\"$c\""))
    ()
  }

  override def run: IO[Unit] = readColors
}
