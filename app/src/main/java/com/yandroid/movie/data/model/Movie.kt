package com.yandroid.movie.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("genre_ids")
    val genresId: List<Int>,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status")
    val status: String,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("status_message")
    val statusMessage: String?,
    @SerializedName("status_code")
    val statusCode: Int,
    val images:Images,
    val similar:Movies,
    val credits:Credits
) {
    val getRunTime: String
        get() {
            if (runtime == null) {
                return "0"
            }
            return "${(runtime / 60)}:${runtime % 60}"
        }
    val getReleaseYear: String
        get() {
            return releaseDate.split('-')[0]
        }
    val getPosterUrl: String
        get() {
            return "https://image.tmdb.org/t/p/w500${posterPath}"
        }
    val getBackdropUrl: String
        get() {
            return "https://image.tmdb.org/t/p/w500${backdropPath}"
        }

    val getGenres: List<String>
        get() {
            val res = ArrayList<String>()
            genresId.forEach {
                res.add(
                    when (it) {
                        28 -> "Action"
                        12 -> "Adventure"
                        16 -> "Animation"
                        35 -> "Comedy"
                        80 -> "Crime"
                        99 -> "Documentary"
                        18 -> "Drama"
                        10751 -> "Family"
                        14 -> "Fantasy"
                        36 -> "History"
                        27 -> "Horror"
                        10402 -> "Music"
                        10749 -> "Romance"
                        878 -> "Science Fiction"
                        10770 -> "TV Movie"
                        53 -> "Thriller"
                        10752 -> "War"
                        37 -> "Western"
                        else -> ""
                    }
                )
            }
            return res
        }
    data class Images(val backdrops:List<Poster> ,val logos:List<Poster> ,val posters:List<Poster>)
}