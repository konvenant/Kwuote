package com.example.my_kwuotes.data.repositories.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.my_kwuotes.data.local.database.FavoriteQuotesDatabase
import com.example.my_kwuotes.data.local.database.QuotesDatabase
import com.example.my_kwuotes.data.local.database.TagsDatabase
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.domain.repository.FavoriteQuoteRepository
import com.example.my_kwuotes.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavQuoteRepositoryImpl @Inject constructor(
    private val database: FavoriteQuotesDatabase,
): FavoriteQuoteRepository {
    override fun getFavQuotes(): Pager<Int, QuoteEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.favDao.pagingSource() }
        )
    }

    override suspend fun addFavQuote(quote: QuoteEntity): String {
        return try {
            database.favDao.addQuote(quote)
            "Quote Added to favorite Successfully"
        } catch (e:Exception){
            e.message.toString()
        }
    }

    override suspend fun deleteFavQuote(quote: QuoteEntity): String {
        return try {
            database.favDao.deleteQuote(quote)
            "Quote Removed from favorite Successfully"
        } catch (e:Exception){
            e.message.toString()
        }
    }

    override suspend fun isFavorite(quoteId: String): Boolean {
        return database.favDao.doesQuoteExist(quoteId) > 0
    }

    override suspend fun deleteQuoteById(quoteId: String): String {
        return try {
            database.favDao.deleteFavQuoteByQuoteId(quoteId)
            "Quote Removed from favorite Successfully"
        } catch (e:Exception){
            e.message.toString()
        }
    }

}