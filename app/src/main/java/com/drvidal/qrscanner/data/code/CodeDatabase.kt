package com.drvidal.qrscanner.data.code

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Code::class],
    version = 1,
    exportSchema = true
)
abstract class CodeDatabase: RoomDatabase() {

    abstract val codeDao: CodeDao

    companion object {
        const val DATABASE_NAME = "codes_database"
    }
}