package com.example.my_kwuotes.data.mappers

import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.models.QuoteX
import com.example.my_kwuotes.data.models.RandomQuote
import com.example.my_kwuotes.data.models.TagsItem
import com.example.my_kwuotes.data.remote.models.QuoteDto

fun QuoteDto.toQuoteEntity(index: Int,category:String) : QuoteEntity {
    return QuoteEntity(
        id = index,
        quoteId = _id,
        author = author,
        authorSlug = authorSlug,
        content = content,
        dateAdded = dateAdded,
        dateModified = dateModified,
        length = length,
        tags = tags,
       category = category
    )
}

fun QuoteEntity.toQuote() : Quote{
    return Quote(
        id,
        quoteId,
        author,
        authorSlug,
        content,
        dateAdded,
        dateModified,
        length,
        tags,
        category
    )
}

fun Quote.toQUoteEntity() : QuoteEntity{
    return QuoteEntity(
        id, quoteId, author, authorSlug, content, dateAdded, dateModified, length, tags, category
    )
}


fun  QuoteEntity.toRandomQuote() : RandomQuote{
    return  RandomQuote(
        quoteId,author, authorSlug, content, dateAdded, dateModified, length, tags
    )
}


fun RandomQuote.toQuote() : Quote{
    return Quote(
        1,
        _id,author,authorSlug,content,dateAdded,dateModified,length,tags,tags[0]
    )
}

fun QuoteX.toQuote() : Quote{
    return  Quote(
        1,
        _id,author,authorSlug,content,dateAdded,dateModified,length,tags,tags[0]
    )
}
