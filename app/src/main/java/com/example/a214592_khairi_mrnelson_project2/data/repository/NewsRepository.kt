package com.example.a214592_khairi_mrnelson_project2.data.repository

import com.example.a214592_khairi_mrnelson_project2.data.local.dao.BookmarkDao
import com.example.a214592_khairi_mrnelson_project2.data.local.entity.BookmarkEntity
import com.example.a214592_khairi_mrnelson_project2.data.remote.NewsApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val apiService: NewsApiService,
    private val bookmarkDao: BookmarkDao,
    private val firestore: FirebaseFirestore
) {
    suspend fun getLiveNews() = apiService.getTopHeadlines()
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
    suspend fun saveBookmark(bookmark: BookmarkEntity) = bookmarkDao.insertBookmark(bookmark)
    suspend fun removeBookmark(bookmark: BookmarkEntity) = bookmarkDao.deleteBookmark(bookmark)
    fun uploadReport(report: Map<String, Any>) {
        firestore.collection("reports").add(report)
    }
}