package com.example.a214592_khairi_mrnelson_project2

import android.app.Application
import androidx.room.Room
import com.example.a214592_khairi_mrnelson_project2.data.local.AppDatabase
import com.google.firebase.FirebaseApp

class VerifiNewsApplication : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "verifinews_database"
        )

            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}