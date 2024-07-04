package com.example.my_kwuotes.data.mappers

import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.models.TagsItem

fun TagsItem.toTagItemEntity(num:Int) : TagsItemEntity {
    return TagsItemEntity(
        num,
        _id,
        dateAdded,
        dateModified,
        name,
        quoteCount,
        slug
    )
}

fun TagsItemEntity.toTagItem() : TagsItem {
    return TagsItem(
        tagId,
        dateAdded, dateModified, name, quoteCount, slug
    )
}