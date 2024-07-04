package com.example.my_kwuotes.data.models

import com.example.my_kwuotes.data.remote.models.QuoteDto

data class TagsQuotes(
    val count: Int,
    val lastItemIndex: Int? = null,
    val page: Int,
    val results: List<QuoteDto>,
    val totalCount: Int,
    val totalPages: Int
)