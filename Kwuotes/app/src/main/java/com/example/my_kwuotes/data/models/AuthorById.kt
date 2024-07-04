package com.example.my_kwuotes.data.models

data class AuthorById(
    val _id: String,
    val bio: String,
    val dateAdded: String,
    val dateModified: String,
    val description: String,
    val link: String,
    val name: String,
    val quoteCount: Int,
    val quotes: List<QuoteX>,
    val slug: String
)