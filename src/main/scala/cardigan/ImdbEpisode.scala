package cardigan

final case class ImdbEpisode(id: String,
                             seasonNumber: String,
                             episodeNumber: String,
                             title: String,
                             image: String,
                             year: String,
                             released: String,
                             plot: String,
                             imDbRating: String,
                             imDbRatingCount: String)
