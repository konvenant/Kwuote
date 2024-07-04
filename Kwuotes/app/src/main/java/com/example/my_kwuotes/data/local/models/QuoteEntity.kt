package com.example.my_kwuotes.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quoteentity")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true)
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