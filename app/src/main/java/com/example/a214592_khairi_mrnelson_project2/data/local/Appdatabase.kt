package com.example.a214592_khairi_mrnelson_project2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a214592_khairi_mrnelson_project2.data.local.dao.BookmarkDao
import com.example.a214592_khairi_mrnelson_project2.data.local.entity.BookmarkEntity

// TUKAR: version = 2
@Database(entities = [BookmarkEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}