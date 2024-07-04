package com.example.my_kwuotes.data.models

data class SearchAuthorResponse(
    val count: Int,
    val lastItemIndex: Any,
    val page: Int,
    val results: List<SearchAuthorResult>,
    val totalCount: Int,
    val totalPages: Int
)