package com.example.a214592_khairi_mrnelson_project2.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a214592_khairi_mrnelson_project2.BuildConfig
import com.example.a214592_khairi_mrnelson_project2.VerifiNewsApplication
import com.example.a214592_khairi_mrnelson_project2.data.local.entity.BookmarkEntity
import com.example.a214592_khairi_mrnelson_project2.data.remote.RetrofitClient
import com.example.a214592_khairi_mrnelson_project2.model.CommentData
import com.example.a214592_khairi_mrnelson_project2.model.NewsItemData
import com.example.a214592_khairi_mrnelson_project2.model.ReportedNewsData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class VerifiNewsViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()

    private val bookmarkDao by lazy {
        (application as VerifiNewsApplication).database.bookmarkDao()
    }

    var loggedInUser by mutableStateOf("")
    var searchQuery by mutableStateOf("")
    var aiFactCheckResult by mutableStateOf("")
    var currentSelectedNews by mutableStateOf<NewsItemData?>(null)
    var userLocation by mutableStateOf("Kesan Lokasi...")

    private val _firebaseReports = mutableStateListOf<ReportedNewsData>()
    val firebaseReports: List<ReportedNewsData> get() = _firebaseReports

    val bookmarkedNews: StateFlow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _allNews = mutableStateListOf<NewsItemData>()
    val allNews: List<NewsItemData> get() = _allNews

    private val apiNewsList = mutableListOf<NewsItemData>()

    init {
        fetchFirebaseReports()
        fetchLiveNewsAPI()
    }

    private fun fetchLiveNewsAPI() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getTopHeadlines()
                val mappedApi = response.articles.mapIndexed { index, article ->
                    NewsItemData(
                        id = "api_$index",
                        imageUrl = article.urlToImage,
                        status = "SAHIH",
                        title = article.title ?: "Tiada Tajuk",
                        description = article.description ?: "Tiada Penerangan",
                        claim = "Sumber: ${article.source?.name ?: "Web"}"
                    )
                }
                apiNewsList.clear()
                apiNewsList.addAll(mappedApi)

                combineNews()
            } catch (e: Exception) {
                Log.e("NewsAPI", "Failed to fetch news: ${e.message}", e)
            }
        }
    }

    private fun fetchFirebaseReports() {
        firestore.collection("reports").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                _firebaseReports.clear()
                _firebaseReports.addAll(it.toObjects(ReportedNewsData::class.java))

                combineNews()
            }
        }
    }

    private fun combineNews() {
        val verifiedReports = _firebaseReports.filter { it.adminVerified }.map { report ->
            NewsItemData(
                id = "fb_${report.id}",
                imageUrl = null,
                imageBase64 = report.photoBase64, // <-- MASUKKAN DATA GAMBAR DARI FIREBASE KE SINI
                status = report.status.uppercase(),
                title = "${report.title}",
                description = report.reason,
                claim = "Dinilai oleh Admin VerifiNews"
            )
        }

        _allNews.clear()
        _allNews.addAll(verifiedReports)
        _allNews.addAll(apiNewsList)
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun submitToFirebase(title: String, reason: String, imageBitmap: Bitmap?) {
        val base64Image = encodeBitmapToBase64(imageBitmap)
        val report = hashMapOf(
            "title" to title,
            "reason" to reason,
            "photoBase64" to base64Image,
            "user" to if (loggedInUser.isEmpty()) "Pengguna Awam" else loggedInUser,
            "status" to "Menunggu Semakan",
            // PERUBAHAN EJAAN: Menggunakan 'adminVerified'
            "adminVerified" to false
        )
        firestore.collection("reports").add(report)
    }

    fun addBookmark(news: NewsItemData) {
        viewModelScope.launch {
            val entity = BookmarkEntity(
                id = news.id,
                title = news.title,
                description = news.description,
                imageUrl = news.imageUrl ?: "",
                sourceName = news.claim,
                publishedAt = "Today",
                status = news.status
            )
            bookmarkDao.insertBookmark(entity)
        }
    }

    // Fungsi untuk memadam bookmark
    fun removeBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            bookmarkDao.deleteBookmark(bookmark)
        }
    }

    fun askAiToVerify(text: String) {
        // Gantikan key statik dengan BuildConfig
        val genModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
        viewModelScope.launch {
            aiFactCheckResult = "Sedang menyemak..."
            try {
                val response = genModel.generateContent("Check if this is fake news: $text")
                aiFactCheckResult = response.text ?: "Tiada maklum balas daripada AI."
            } catch (e: Exception) {
                if (e.message?.contains("429") == true) {
                    aiFactCheckResult = "Ralat: Terlalu banyak permintaan! Sila tunggu seminit sebelum cuba lagi (Had Pelan Percuma)."
                } else {
                    aiFactCheckResult = "Ralat API (${e.localizedMessage ?: "Unknown Code"}): Sila periksa konfigurasi pelan anda."
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun detectLocation(context: Context) {
        val client = LocationServices.getFusedLocationProviderClient(context)
        client.lastLocation.addOnSuccessListener { loc ->
            userLocation = loc?.let { "${it.latitude}, ${it.longitude}" } ?: "Location unavailable"
        }
    }

    fun login(u: String) { loggedInUser = u }
    fun logout() { loggedInUser = "" }
    fun selectNews(n: NewsItemData) { currentSelectedNews = n }
    fun updateSearch(q: String) { searchQuery = q }

    fun submitComment(newsId: String, text: String) {
        val news = currentSelectedNews ?: return
        val newComment = CommentData(
            id = news.comments.size + 1,
            username = loggedInUser.ifEmpty { "Pengguna Awam" },
            text = text
        )
        currentSelectedNews = news.copy(comments = news.comments + newComment)
    }

    fun toggleLikeComment(newsId: String, commentId: Int) {
        val news = currentSelectedNews ?: return
        val updatedComments = news.comments.map {
            if (it.id == commentId) {
                val newLikeState = !it.isLikedByMe
                it.copy(
                    isLikedByMe = newLikeState,
                    likes = if (newLikeState) it.likes + 1 else it.likes - 1
                )
            } else it
        }
        currentSelectedNews = news.copy(comments = updatedComments)
    }
}