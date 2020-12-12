package com.example.browsing

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Entity(tableName = TABLE_NAME_CATS)
data class Cat(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "url") val url: String?
)

class CatListTypeConverter {
    @TypeConverter
    fun fromString(serialisedList: String): List<Cat> {
        val listType: Type = object : TypeToken<List<Cat>?>() {}.type
        return GsonBuilder().create().fromJson(serialisedList, listType)
    }

    @TypeConverter
    fun toString(list: List<Cat>): String {
        return GsonBuilder().create().toJson(list)
    }
}