package cardigan

final case class ImdbShow(imDbId: String,
                          title: String,
                          fullTitle: String,
                          `type`: String,
                          year: String,
                          episodes: List[ImdbEpisode],
                          errorMessage: String)
