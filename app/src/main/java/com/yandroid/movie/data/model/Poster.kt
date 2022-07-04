package com.yandroid.movie.data.model

import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("aspect_ratio")
    val ratio: Double,
    @SerializedName("file_path")
    val path: String,
    val height: Int,
    val width: Int
) {
    val getPath: String
        get() {
            return "https://image.tmdb.org/t/p/w500${path}"
        }
}