package cardigan

import cats.effect.{ IO, IOApp }
import sys.process._

import java.io.File
import java.net.URL
import scala.language.postfixOps

/** Download episode metadata for each season from IMDb API. */
object EpisodeGuideDownloader extends IOApp.Simple {
  private val IMDB_API_KEY = sys.env.getOrElse("IMDB_API_KEY", "MISSING_IMDB_API_KEY")
  private val misterRogersShowId = "tt0062588"

  private val downloader = IO {
    def downloadFile(n: Int): Unit = {
      val url = new URL(s"https://imdb-api.com/API/SeasonEpisodes/$IMDB_API_KEY/$misterRogersShowId/$n")
      val id = "%02d".format(n)
      val file = new File(s"./src/main/resources/guide_s$id.json")
      println(s"Downloading $url to $file...")
      url #> file !!
    }

    (1 to 31).foreach(downloadFile)
  }

  override def run: IO[Unit] = downloader
}
