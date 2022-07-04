package com.yandroid.movie.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Movie
import java.lang.Exception

class SearchPagingSource(private val api:ApiService, private val query: String):PagingSource<Int,Movie>() {
    override fun getRefreshKey(state: PagingState<Int,Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,Movie> {
        return try {
            val currentPage = params.key ?: 1
            val result = api.searchMovie(key = APIKEY,query=query, page = currentPage)

            if (!result.statusMessage.isNullOrEmpty())
                throw Exception(result.statusMessage)

            LoadResult.Page(
                result.result,
                if (currentPage == 1) null else currentPage - 1,
                if (currentPage == result.totalPage) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}