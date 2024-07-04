package com.example.my_kwuotes.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.AuthorById
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.models.RandomQuotes
import com.example.my_kwuotes.data.models.SearchAuthorResponse
import com.example.my_kwuotes.data.models.SearchAuthorResult
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.models.Tags
import com.example.my_kwuotes.data.remote.models.QuoteDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

// QuoteRepository.kt
interface QuoteRepository {
    fun getQuotesByCategory(category: String): Flow<PagingData<QuoteEntity>>

    fun getMyQuotes(): Pager<Int, QuoteEntity>
    fun getAllQuotesAsList() : Flow<PagingData<QuoteEntity>>
    suspend fun getRandomQuote() : Response<RandomQuote>

    fun getQuotesByAuthor(author: String): Pager<Int, QuoteDto>

    fun getSearchQuote(query: String) : Pager<Int,SearchResult>

    fun getSearchAuthors(query: String) : Pager<Int,SearchAuthorResult>

    fun getAuthors() : Pager<Int, Author>
    suspend fun getTags() : Response<Tags>

    suspend fun getRandomQuotes() : Response<RandomQuotes>

    suspend fun getQuoteById(quoteId: String, category: String): Quote

    suspend fun addMyQuote(quote: QuoteEntity): String

    suspend fun deleteMyQuote(quote: QuoteEntity): String

    suspend fun getRandomQuoteFromDb(): QuoteEntity?

    suspend fun getAuthorById(authorId: String) : Response<AuthorById>

    suspend fun getAuthorBySlug(slug: String) : Response<SearchAuthorResponse>
}
