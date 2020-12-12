package com.example.catastrophic

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.browsing.Cat
import com.example.browsing.CatListTypeConverter
import com.example.browsing.CatsDao

const val DATABASE_NAME = "catastrophic-db"
@Database(entities = [Cat::class], version = 1)
@TypeConverters(CatListTypeConverter::class)
abstract class CatastrophicDataBase: RoomDatabase() {
    abstract fun catsDao(): CatsDao
}