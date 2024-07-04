package com.example.my_kwuotes.data.remote.mediators

import android.util.Log
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
class QuoteRemoteMediator @Inject constructor (
    private val quoteDb: QuotesDatabase,
    private val quoteApi: QuoteApi,
    private val category: String
) : RemoteMediator<Int, QuoteEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QuoteEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->  return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    Log.e("CALLED APPEND ","Called APPEND")
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        Log.e("LAST ITEM Reacehed","Last Item: $lastItem")
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
//                 1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            Log.e("LOAD KEY","Loading page: $loadKey")



            val quotes = quoteApi.getQuotesByCategory(
                tags = category,
                page = loadKey
            )

            quoteDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    Log.d("QuoteDbTransaction", "Clearing all quotes")
                    quoteDb.quoteDao.clearQuotesByCategory(category)
                }
                val quoteEntities = quotes.results.mapIndexed { index, quoteDto ->
                    val startIndex = if (loadType == LoadType.REFRESH) 1 else (loadKey - 1) * state.config.pageSize + 1
                    quoteDto.toQuoteEntity(index+startIndex,category)
                }
                quoteEntities.mapIndexed { i, it ->
                    Log.e("ENTITY","at $i  is id=${it.id} ")
                }
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