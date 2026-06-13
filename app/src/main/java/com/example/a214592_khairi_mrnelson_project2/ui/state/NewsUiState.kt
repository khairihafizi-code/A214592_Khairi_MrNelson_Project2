package com.example.a214592_khairi_mrnelson_project2.ui.state

import com.example.a214592_khairi_mrnelson_project2.data.remote.Article

data class NewsUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val errorMessage: String? = null,
    val userLocation: String = "Detecting...",
    val aiResponse: String = ""
)