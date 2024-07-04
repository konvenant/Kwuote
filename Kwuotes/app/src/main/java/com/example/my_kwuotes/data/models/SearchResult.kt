package com.example.my_kwuotes.data.models

data class SearchResult(
    val _id: String,
    val author: String,
    val authorId: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int,
    val tags: List<String>
)

