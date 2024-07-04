package com.example.my_kwuotes.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.my_kwuotes.data.local.dao.QuoteDao
import com.example.my_kwuotes.data.local.dao.TagDao
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.remote.models.QuoteDto

@Database(
    entities = [TagsItemEntity::class],
    version = 1,
    exportSchema = false,
//    autoMigrations = [
//        AutoMigration(1,2)
//    ]
)
abstract class TagsDatabase : RoomDatabase() {
    // QuoteDatabase.kt
        abstract val tagDao: TagDao
}