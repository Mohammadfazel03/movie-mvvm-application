package com.yandroid.movie.data.model

import com.google.gson.annotations.SerializedName

data class Genre(
    val id: Int,
    val name: String
)

data class Genres(
    val genres: List<Genre>,
    @SerializedName("statusMessage")
    val statusMessage: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)