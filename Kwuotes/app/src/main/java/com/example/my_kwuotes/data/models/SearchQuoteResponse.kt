package com.example.my_kwuotes.data.models

data class SearchQuoteResponse(
    val __info__: Info,
    val count: Int,
    val page: Int,
    val results: List<SearchResult>,
    val totalCount: Int,
    val totalPages: Int
)