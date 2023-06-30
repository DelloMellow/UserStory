package com.example.userstory.di

import android.content.Context
import com.example.userstory.ApiConfig
import com.example.userstory.data.StoryRepository
import com.example.userstory.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}