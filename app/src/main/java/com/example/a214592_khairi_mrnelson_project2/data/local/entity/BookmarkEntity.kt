package com.example.a214592_khairi_mrnelson_project2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val sourceName: String,
    val publishedAt: String,
    val status: String
)