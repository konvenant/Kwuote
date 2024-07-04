package com.example.my_kwuotes.data.mappers

import com.example.my_kwuotes.data.models.Author
import com.example.my_kwuotes.data.models.SearchAuthorResult

fun SearchAuthorResult.toAuthor() : Author{
    return Author(
        _id, bio, dateAdded, dateModified, description, link, name, quoteCount, slug
    )
}