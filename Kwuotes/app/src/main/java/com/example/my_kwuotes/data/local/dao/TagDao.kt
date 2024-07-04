package com.example.my_kwuotes.data.local.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tagsitementity")
    fun getAllUserTags(): Flow<List<TagsItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tags: List<TagsItemEntity>)

    @Query("DELETE FROM tagsitementity")
    suspend fun clearAll()


}