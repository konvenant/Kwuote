package com.example.my_kwuotes.data.local.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.my_kwuotes.data.local.dao.QuoteDao
import com.example.my_kwuotes.data.local.models.QuoteEntity
import com.example.my_kwuotes.data.remote.models.QuoteDto


@Database(
    entities = [QuoteEntity::class],
    version = 4,  // Update the version to 4
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4)
    ]
)
@TypeConverters(Converters::class)
abstract class QuotesDatabase : RoomDatabase() {

    abstract val quoteDao: QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: QuotesDatabase? = null

        fun getDatabase(context: Context): QuotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuotesDatabase::class.java,
                    "quote.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}



val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create a new table with the updated schema
        database.execSQL("""
            CREATE TABLE quoteentity_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                quoteId TEXT NOT NULL,
                author TEXT NOT NULL,
                authorSlug TEXT NOT NULL,
                content TEXT NOT NULL,
                dateAdded TEXT NOT NULL,
                dateModified TEXT NOT NULL,
                length INTEGER NOT NULL,
                tags TEXT NOT NULL,
                category TEXT NOT NULL
            )
        """.trimIndent())

        // Copy data from the old table to the new table
        database.execSQL("""
            INSERT INTO quoteentity_new (id, quoteId, author, authorSlug, content, dateAdded, dateModified, length, tags, category)
            SELECT id, quoteId, author, authorSlug, content, dateAdded, dateModified, length, tags, category
            FROM quoteentity
        """.trimIndent())

        // Remove the old table
        database.execSQL("DROP TABLE quoteentity")

        // Rename the new table to the old table's name
        database.execSQL("ALTER TABLE quoteentity_new RENAME TO quoteentity")
    }
}



