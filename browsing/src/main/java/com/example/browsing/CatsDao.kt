package com.example.browsing

import androidx.room.*

const val TABLE_NAME_CATS = "cats"
@Dao
abstract class CatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRows(entries: List<Cat>)

    @Query("DELETE FROM $TABLE_NAME_CATS")
    abstract suspend fun clear()

    @Query("SELECT * FROM $TABLE_NAME_CATS LIMIT (:limit) OFFSET (:offset)")
    abstract suspend fun getRowsWith(limit: Int, offset: Int): List<Cat>

    @Query("SELECT * FROM $TABLE_NAME_CATS")
    abstract suspend fun getAllRows(): List<Cat>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME_CATS")
    abstract suspend fun getRowsCount(): Int
}