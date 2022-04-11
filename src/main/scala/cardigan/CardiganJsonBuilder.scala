package cardigan

import cats.effect.{ IO, IOApp }
import io.circe.parser.decode
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.io.{ File, PrintWriter }
import java.nio.file.Files
import scala.io.Source

/** Write a JSON object file where the keys are the colors and the values are the episodes. */
object CardiganJsonBuilder extends IOApp.Simple {

  private val builder = IO {
    val episodeColorMap = buildEpisodeColorMap()
    val episodes = buildEpisodeList()
    val colorEpisodes = buildCardiganEpisodes(episodes, episodeColorMap)
    // TODO CardiganEpisode should parse date
    colorEpisodes.foreach(ep => println(ep.asJson.noSpaces))
    println(s"Found ${colorEpisodes.size} color episodes out of ${episodes.size} total episodes")
    val episodesByColorFile = new File(s"./src/main/resources/episodes_by_color.json")
    val writer = new PrintWriter(episodesByColorFile)
    val episodesByColor = colorEpisodes.groupMap(_.color)(identity)
    writer.write(episodesByColor.asJson.spaces2)
    writer.close()
  }

  private def buildCardiganEpisodes(episodes: List[ImdbEpisode], episodeColorMap: Map[String, String]) = {
    episodes.flatMap { imdbEpisode =>
      val idOpt = imdbEpisode.title match {
        case title if title.length == 4 => Some(title)
        case title if title.indexOf(":") == 4 => Some(title.substring(0, 4))
        case title =>
          println(title)
          None
      }
      for {
        id <- idOpt
        color <- episodeColorMap.get(id)
      } yield {
        CardiganEpisode(id, imdbEpisode, color)
      }
    }
  }

  private def buildEpisodeColorMap(): Map[String, String] = {
    val ignore = Set("colorcode", "")
    val input = Source.fromFile("./src/main/resources/clean_cardigancolors.csv")
    val tuples = input.getLines()
      .drop(1) // skip header
      .filter(_.nonEmpty)
      .map(line => line.split(","))
      .filter(_.length > 2)
      .map(cells => cells(0) -> s"#${cells(2)}")
      .map { case (episodeId, color) =>
        // Combine these 2 similar colors.
        if (color == "#526083") (episodeId, "#56667d")
        else (episodeId, color)
      }
      .filterNot { case (_, color) => color == "#" }
      .toList
    input.close()
    tuples.toMap
  }

  private def buildEpisodeList(): List[ImdbEpisode] = {
    val files = (1 to 31).map { n =>
      val id = "%02d".format(n)
      new File(s"./src/main/resources/guide_s$id.json")
    }.toList
    val seasonEpisodes = for {
      file <- files
    } yield {
      decode[ImdbShow](Files.readString(file.toPath)) match {
        case Left(_) => List.empty[ImdbEpisode]
        case Right(show) => show.episodes
      }
    }
    seasonEpisodes.flatten
  }

  override def run: IO[Unit] = builder
}
