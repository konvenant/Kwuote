package com.example.my_kwuotes.data.remote.api

import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.AuthorById
import com.example.my_kwuotes.data.models.Authors
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.models.RandomQuotes
import com.example.my_kwuotes.data.models.SearchAuthorResponse
import com.example.my_kwuotes.data.models.SearchQuoteResponse
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.models.Tags
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.data.remote.models.QuotesFromCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuoteApi {

    @GET("/quotes")
    suspend fun getQuotesByCategory(
        @Query("tags") tags: String,
        @Query("page") page: Int
    ): QuotesFromCategory

    @GET("/quotes")
    suspend fun getQuotesByAuthor(
        @Query("author") author: String,
        @Query("page") page: Int
    ): QuotesFromCategory

    @GET("/quotes/{id}")
    suspend fun getQuoteById(
        @Path("id") id: String
    ): Response<SearchResult>

    @GET("/random")
    suspend fun getRandomQuote() : Response<RandomQuote>

    @GET("/search/quotes")
    suspend fun searchQuotes(
        @Query("query") query: String,
        @Query("page") page: Int
    ) : SearchQuoteResponse

    @GET("/search/authors")
    suspend fun searchAuthors(
        @Query("query") query: String,
        @Query("page") page: Int
    ) : SearchAuthorResponse

    @GET("/authors")
    suspend fun getAuthors(
        @Query("page") page: Int,
        @Query("order") order: String = "asc"
    ) : Authors

    @GET("/authors/{id}")
    suspend fun getAuthorsById(
        @Path("id") id: String
    ) : Response<AuthorById>


    @GET("/authors")
    suspend fun getAuthorBySlug(
        @Query("slug") slug: String
    ) : Response<SearchAuthorResponse>

    @GET("/tags")
    suspend fun getTags() : Response<Tags>

    @GET("/quotes/random")
    suspend fun getRandomQuotes(): Response<RandomQuotes>
}