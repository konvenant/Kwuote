package com.example.my_kwuotes.data.local.database

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn(fromColumnName = "_id", toColumnName = "quoteId", tableName = "quoteentity")
class Migration3to4Spec: AutoMigrationSpec