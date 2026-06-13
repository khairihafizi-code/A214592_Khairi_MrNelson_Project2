package com.example.a214592_khairi_mrnelson_project2.data.remote

import com.example.a214592_khairi_mrnelson_project2.BuildConfig // Pastikan import ini wujud
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getTopHeadlines(
        @Query("q") query: String = "malaysia",
        // Gantikan key statik dengan BuildConfig
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): NewsResponse
}

data class NewsResponse(val articles: List<Article>)
data class Article(
    val source: Source?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val content: String?
)
data class Source(val name: String?)