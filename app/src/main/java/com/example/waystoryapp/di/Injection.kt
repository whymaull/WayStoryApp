package com.example.waystoryapp.di

import android.content.Context
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.pref.UserPreference
import com.example.waystoryapp.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}