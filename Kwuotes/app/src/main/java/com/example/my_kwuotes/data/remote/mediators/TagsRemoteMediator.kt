package com.example.my_kwuotes.data.remote.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.my_kwuotes.data.local.database.QuotesDatabase
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.remote.api.QuoteApi
import com.example.my_kwuotes.data.mappers.toQuoteEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TagsRemoteMediator @Inject constructor(
    private val quoteDb: QuotesDatabase,
    private val quoteApi: QuoteApi,
    private val category: String
) : RemoteMediator<Int, QuoteEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QuoteEntity>
    ): MediatorResult {
        var page: Int  = 1
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                       page

                    }
                }
            }

            val quotes = quoteApi.getQuotesByCategory(
                tags = category,
                page = state.config.pageSize
            )

            page = quotes.page + 1

            quoteDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    quoteDb.quoteDao.clearAll()
                }
                val quoteEntities = quotes.results.map { it.toQuoteEntity(1,"ttt") }
                quoteDb.quoteDao.upsertAll(quoteEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = quotes.results.isEmpty()
            )
        } catch(e: IOException) {
            MediatorResult.Error(e)
        } catch(e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}