package com.example.waystoryapp.di

import android.content.Context
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.database.StoryDB
import com.example.waystoryapp.pref.UserPreference
import com.example.waystoryapp.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val database = StoryDB.getDatabase(context)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(database,apiService,user.token,pref)
    }
}