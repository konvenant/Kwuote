package com.example.my_kwuotes.data.remote.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.SearchQuoteResponse
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.remote.api.QuoteApi
import com.example.my_kwuotes.data.remote.models.QuoteDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GetAuthorsPagingSource @Inject constructor(
    private val quoteApi: QuoteApi
) : PagingSource<Int, Author>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Author> {
        val position = params.key ?: 1
        return try {
            val quotes = quoteApi.getAuthors(position)
            LoadResult.Page(
                data = quotes.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (quotes.results.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Author>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}