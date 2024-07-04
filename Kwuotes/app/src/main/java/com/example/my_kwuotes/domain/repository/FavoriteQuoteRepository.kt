package com.example.my_kwuotes.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.my_kwuotes.data.local.models.QuoteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteQuoteRepository {
    fun getFavQuotes(): Pager<Int, QuoteEntity>

    suspend fun addFavQuote(quote: QuoteEntity): String

    suspend fun deleteFavQuote(quote: QuoteEntity): String

    suspend fun isFavorite(quoteId: String) :  Boolean

    suspend fun deleteQuoteById(quoteId: String): String
}