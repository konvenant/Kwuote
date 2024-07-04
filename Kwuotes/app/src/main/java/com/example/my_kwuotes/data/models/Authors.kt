package com.example.my_kwuotes.data.models

data class Authors(
    val count: Int,
    val lastItemIndex: Int? = null,
    val page: Int,
    val results: List<Author>,
    val totalCount: Int,
    val totalPages: Int
)