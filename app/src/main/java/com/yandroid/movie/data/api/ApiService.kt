package com.yandroid.movie.data.api

import com.yandroid.movie.data.model.Genres
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.model.Movies
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val APIKEY = "YOUR-API-KEY"

interface ApiService {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovie(@Query("api_key") key: String, @Query("page") page: Int): Movies

    @GET("movie/popular")
    suspend fun getPopularMovie(@Query("api_key") key: String, @Query("page") page: Int): Movies

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovie(@Path("movieId") movieId: Int,@Query("api_key") key: String, @Query("page") page: Int): Movies

    @GET("movie/upcoming")
    suspend fun getUpcomingMovie(@Query("api_key") key: String, @Query("page") page: Int): Movies

    @GET("trending/movie/week")
    suspend fun getTrendingMovie(@Query("api_key") key: String): Movies

    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") key: String): Genres

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Movies

    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("api_key") key: String,
        @Query("language") language: String? = null,
        @Query("region") region: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("primary_release_date.gte") minReleaseDate: String? = null,
        @Query("primary_release_date.lte") maxReleaseDate: String? = null,
        @Query("with_release_type") releaseType: Int? = null,
        @Query("vote_count.gte") minVoteCount: Int? = null,
        @Query("vote_count.lte") maxVoteCount: Int? = null,
        @Query("vote_average.gte") minScore: Int? = null,
        @Query("vote_average.lte") maxScore: Int? = null,
        @Query("with_people") withPeople: String? = null,
        @Query("with_genres") withGenres: String? = null,
        @Query("with_keywords") withKeywords: String? = null,
        @Query("watch_region") watchRegion: String? = null, @Query("page") page: Int
    ): Movies

    @GET("movie/{movieId}")
    suspend fun getMovie(
        @Path("movieId") movieId: Int,
        @Query("api_key") key: String,
        @Query("include_image_language") imageLanguage: String? = "en,null",
        @Query("append_to_response") appendToResponse: String? = "images,similar,credits"
    ): Movie

}

fun createApiServiceInstance(): ApiService = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(GsonConverterFactory.create())
    .build().create(ApiService::class.java)
