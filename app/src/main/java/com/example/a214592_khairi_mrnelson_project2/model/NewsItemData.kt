package com.example.a214592_khairi_mrnelson_project2.model

import com.google.firebase.firestore.DocumentId // <-- IMPORT PENTING INI

data class NewsItemData(
    val id: String,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val imageBase64: String? = null, // <-- TAMBAH BARIS INI
    val status: String,
    val title: String,
    val description: String,
    val claim: String = "Sedang disemak",
    var comments: List<CommentData> = emptyList()
)

data class CommentData(
    val id: Int,
    val username: String,
    val text: String,
    var likes: Int = 0,
    var isLikedByMe: Boolean = false
)

data class ReportedNewsData(
    @DocumentId val id: String = "", // <-- Tanda ini wajib untuk tarik ID dari Firestore!
    val title: String = "",
    val reason: String = "",
    val location: String = "",
    val user: String = "",
    val status: String = "Menunggu Semakan",
    val adminVerified: Boolean = false, // <-- NAMA BARU (Buang awalan "is")
    val photoBase64: String? = null
)