package com.yandroid.movie.data.model

import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val result: List<Movie>,
    @SerializedName("total_results")
    val totalResult: Int,
    @SerializedName("total_page")
    val totalPage: Int,
    @SerializedName("statusMessage")
    val statusMessage: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)
