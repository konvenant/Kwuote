package com.example.my_kwuotes.data.repositories.remote

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.my_kwuotes.data.local.database.QuotesDatabase
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.models.RandomQuotes
import com.example.my_kwuotes.data.models.SearchAuthorResult
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.models.Tags
import com.example.my_kwuotes.data.remote.mediators.QuoteRemoteMediator
import com.example.my_kwuotes.data.remote.api.QuoteApi
import com.example.my_kwuotes.data.remote.mediators.AuthorQuotesPagingSource
import com.example.my_kwuotes.data.remote.mediators.GetAuthorsPagingSource
import com.example.my_kwuotes.data.remote.mediators.SearchAuthorsPagingSource
import com.example.my_kwuotes.data.remote.mediators.SearchQuotesPagingSource
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.data.mappers.toQuote
import com.example.my_kwuotes.data.models.AuthorById
import com.example.my_kwuotes.data.models.SearchAuthorResponse
import com.example.my_kwuotes.domain.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

// QuoteRepositoryImpl.kt
class QuoteRepositoryImpl @Inject constructor(
    private val apiService: QuoteApi,
    private val database: QuotesDatabase,
    private val context: Context
) : QuoteRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getQuotesByCategory(category: String): Flow<PagingData<QuoteEntity>> {
        val pagingSourceFactory = { database.quoteDao.getQuotesByCategory(category) }

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = QuoteRemoteMediator(database,apiService,category),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getMyQuotes(): Pager<Int, QuoteEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.quoteDao.getQuotesByCategory("my_quotes") }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllQuotesAsList(): Flow<PagingData<QuoteEntity>> {
        val pagingSourceFactory = { database.quoteDao.pagingSource() }

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = QuoteRemoteMediator(database,apiService,"Wisdom"),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getRandomQuote(): Response<RandomQuote> {
      return  apiService.getRandomQuote()
    }



    override fun getQuotesByAuthor(author: String): Pager<Int, QuoteDto> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AuthorQuotesPagingSource(apiService,author) }
        )
    }

    override fun getSearchQuote(query: String): Pager<Int, SearchResult> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchQuotesPagingSource(apiService, query) }
        )
    }

    override fun getSearchAuthors(query: String): Pager<Int, SearchAuthorResult> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchAuthorsPagingSource(apiService, query) }
        )
    }

    override fun getAuthors(): Pager<Int, Author> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetAuthorsPagingSource(apiService) }
        )
    }

    override suspend fun getTags() : Response<Tags> {
        return apiService.getTags()
    }

    override suspend fun getRandomQuotes(): Response<RandomQuotes> {
        return apiService.getRandomQuotes()
    }

    override suspend fun getQuoteById(quoteId: String, category: String): Quote {
        val emptyQuote = Quote(
            id = 0,
            quoteId = "",
            author = "",
            authorSlug = "",
            content = "",
            dateAdded = "",
            dateModified = "",
            length = 0,
            tags = emptyList(),
            category = category
        )

        return try {
            // Attempt to fetch from API
            val response = apiService.getQuoteById(quoteId)
            if (response.isSuccessful) {
                val q = response.body()!!
                val newQuote = Quote(
                    id = 0,
                    quoteId = q._id,
                    author = q.author,
                    authorSlug = q.authorSlug,
                    content = q.content,
                    dateAdded = q.dateAdded,
                    dateModified = q.dateModified,
                    length = q.length,
                    tags = q.tags,
                    category = category
                )

                // Save to database for future use
//                database.quoteDao.insertQuote(newQuote.toQuoteEntity())

                newQuote
            } else {
                // Fetch from database if API response is not successful
                database.quoteDao.getQuotesByQuoteId(quoteId).toQuote() ?: emptyQuote
            }
        } catch (e: Exception) {
            // Fetch from database if an exception occurs
            withContext(Dispatchers.IO) {
                database.quoteDao.getQuotesByQuoteId(quoteId).toQuote() ?: emptyQuote
            }
        }
    }



    override suspend fun addMyQuote(quote: QuoteEntity): String {
        return try {
            val quotes = listOf(quote)
            database.quoteDao.upsertAll(quotes)
            "Quote Added Successfully"
        } catch (e:Exception){
            e.message.toString()
        }
    }

    override suspend fun deleteMyQuote(quote: QuoteEntity): String {
        return try {
            database.quoteDao.deleteMyQuote(quote)
            "Quote Deleted Successfully"
        } catch (e:Exception){
            e.message.toString()
        }
    }

    override suspend fun getRandomQuoteFromDb(): QuoteEntity? {

        return database.quoteDao.getRandomQuote()

    }

    override suspend fun getAuthorById(authorId: String): Response<AuthorById> {
        return apiService.getAuthorsById(authorId)
    }

    override suspend fun getAuthorBySlug(slug: String): Response<SearchAuthorResponse> {
        return apiService.getAuthorBySlug(slug)
    }


}
