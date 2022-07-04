package com.yandroid.movie.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Movie
import java.lang.Exception

class SimilarPagingSource(private val api: ApiService , private val movieId:Int) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val result = api.getSimilarMovie(movieId,APIKEY,currentPage)

            if (!result.statusMessage.isNullOrEmpty())
                throw Exception(result.statusMessage)

            PagingSource.LoadResult.Page(
                result.result,
                if (currentPage == 1) null else currentPage - 1,
                if (currentPage == result.totalPage) null else currentPage + 1
            )
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}