package com.example.my_kwuotes.data.repositories.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.my_kwuotes.data.local.database.QuotesDatabase
import com.example.my_kwuotes.data.local.database.TagsDatabase
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val database: TagsDatabase,
): TagRepository {
    override suspend fun getUserTags(): Flow<List<TagsItemEntity>> {
      return database.tagDao.getAllUserTags()
    }

    override suspend fun addUserTags(tags: List<TagsItemEntity>): String {
        return try {
            database.tagDao.upsertAll(tags)
            "user categories saved successfully"
        } catch (e:Exception){
            e.message.toString()
        }
    }

    override suspend fun clearAllTags() {
       database.tagDao.clearAll()
    }
}