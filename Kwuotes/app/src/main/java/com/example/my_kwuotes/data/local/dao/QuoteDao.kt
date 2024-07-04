package com.example.my_kwuotes.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_kwuotes.data.local.models.QuoteEntity

@Dao
interface QuoteDao {
//    @Upsert
//    suspend fun upsertAll(quotes: List<QuoteEntity>)
    @Query("SELECT * FROM quoteentity WHERE category = :category")
     fun getQuotesByCategory(category: String): PagingSource<Int,QuoteEntity>

    @Query("SELECT * FROM quoteentity WHERE quoteId = :quoteId")
    fun getQuotesByQuoteId(quoteId: String): QuoteEntity
//    SELECT * FROM users WHERE label LIKE :query
    @Query("DELETE FROM quoteentity")
    suspend fun clearAll()

    @Query("SELECT * FROM quoteentity")
    fun pagingSource(): PagingSource<Int,QuoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(quotes: List<QuoteEntity>)
    @Query("DELETE FROM quoteentity WHERE category = :category")
    suspend fun clearQuotesByCategory(category: String)

    @Delete
    suspend fun deleteMyQuote(quoteEntity: QuoteEntity)

    @Query("SELECT * FROM quoteentity ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuote(): QuoteEntity?
}