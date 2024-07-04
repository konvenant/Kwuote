package com.example.my_kwuotes.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tagsitementity")
data class TagsItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int ,
    val tagId: String,
    val dateAdded: String,
    val dateModified: String,
    val name: String,
    val quoteCount: Int,
    val slug: String
)