package com.example.my_kwuotes.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    suspend fun getUserTags() : Flow<List<TagsItemEntity>>

    suspend fun addUserTags(tags: List<TagsItemEntity>): String

    suspend fun clearAllTags()

}