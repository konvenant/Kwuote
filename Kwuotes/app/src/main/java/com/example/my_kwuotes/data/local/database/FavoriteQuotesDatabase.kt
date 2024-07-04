package com.example.my_kwuotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.my_kwuotes.data.local.dao.FavoriteQuotesDao
import com.example.my_kwuotes.data.local.models.QuoteEntity

@Database(entities = [QuoteEntity::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavoriteQuotesDatabase : RoomDatabase() {
    abstract val favDao : FavoriteQuotesDao
}