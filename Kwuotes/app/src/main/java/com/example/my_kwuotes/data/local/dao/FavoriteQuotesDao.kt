package com.example.my_kwuotes.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.models.Quote

@Dao
interface FavoriteQuotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuote(quotes: QuoteEntity)

    @Query("SELECT * FROM quoteentity")
    fun pagingSource(): PagingSource<Int, QuoteEntity>
    @Delete
    suspend fun deleteQuote(quote: QuoteEntity)

    @Query("SELECT COUNT(*) FROM quoteentity WHERE quoteId = :quoteId")
    suspend fun doesQuoteExist(quoteId: String): Int

    @Query("DELETE FROM quoteentity WHERE quoteId = :quoteId")
    suspend fun deleteFavQuoteByQuoteId(quoteId: String): Int

}