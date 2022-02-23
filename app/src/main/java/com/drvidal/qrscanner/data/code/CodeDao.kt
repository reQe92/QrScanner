package com.drvidal.qrscanner.data.code

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeDao {

    @Query("SELECT * FROM Code ORDER BY timestamp DESC")
    fun getCodes(): Flow<List<Code>>

    @Query("SELECT * FROM Code WHERE id = :id")
    suspend fun getCodeById(id: Int): Code?

    @Query("SELECT id FROM Code ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastCodeInserted(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCode(code: Code)

    @Delete
    suspend fun deleteCode(code: Code)

}