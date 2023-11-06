package com.example.waystoryapp.data.reps

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.waystoryapp.data.api.ApiService
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.database.StoryDB
import com.example.waystoryapp.data.tools.RemoteMediatorStory
import com.example.waystoryapp.pref.UserModel
import com.example.waystoryapp.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class UserRepository private constructor(
    private val token:String,
    private val userPreference: UserPreference,
    private val db: StoryDB,
    private val apiService: ApiService,
) {
    fun getQuote(): LiveData<PagingData<Entities>> {
        userPreference.getSession()
        val user = runBlocking { userPreference.getSession().first() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 9
            ),
            remoteMediator = RemoteMediatorStory(user.token,db, apiService),
            pagingSourceFactory = {
                db.storyDao().getListStoryPaging()
            }
        ).liveData
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
              db: StoryDB,
              apiService: ApiService,
              token:String,
            userPreference: UserPreference,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(token,userPreference,db,apiService)
            }.also { instance = it }
    }
}