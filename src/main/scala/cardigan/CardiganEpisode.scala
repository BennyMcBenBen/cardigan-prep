package cardigan

final case class CardiganEpisode(id: String,
                                 imdbEpisodeId: String,
                                 seasonNumber: Int,
                                 episodeNumber: Int,
                                 title: String,
                                 image: String,
                                 year: Int,
                                 released: String,
                                 plot: String,
                                 color: String)

object CardiganEpisode {
  def apply(id: String, imdbEpisode: ImdbEpisode, color: String): CardiganEpisode = {
    CardiganEpisode(id, imdbEpisode.id,
      imdbEpisode.seasonNumber.toInt,
      imdbEpisode.episodeNumber.toInt,
      imdbEpisode.title,
      imdbEpisode.image,
      imdbEpisode.year.toInt,
      imdbEpisode.released,
      imdbEpisode.plot,
      color)
  }
}
