package com.example.my_kwuotes.data.remote.models

data class QuotesFromCategory(
    val count: Int,
    val lastItemIndex: Int? = null,
    val page: Int,
    val results: List<QuoteDto>,
    val totalCount: Int,
    val totalPages: Int
)