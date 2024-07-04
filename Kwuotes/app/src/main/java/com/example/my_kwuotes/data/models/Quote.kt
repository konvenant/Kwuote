package com.example.my_kwuotes.data.models

data class Quote(
    val id: Int,
    val quoteId: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int,
    val tags: List<String>,
    val category: String
)