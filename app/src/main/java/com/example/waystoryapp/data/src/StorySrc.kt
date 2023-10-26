package com.example.waystoryapp.data.src

import com.example.waystoryapp.data.api.ApiResponse
import com.example.waystoryapp.data.local.dao.StoryDao
import com.example.waystoryapp.data.remote.story.AddStoryResponse
import com.example.waystoryapp.data.remote.story.GetStoryResponse
import com.example.waystoryapp.data.remote.story.StoryService
import com.example.waystoryapp.data.tools.storyToentity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StorySrc constructor(
    private val storyDao: StoryDao,
    private val storyService: StoryService
) {

    suspend fun getAllStories(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.getAllStory(token)
                if (!response.error) {
                    storyDao.deleteAllStories()
                    val storyList = response.listStory.map {
                        storyToentity(it)
                    }
                    storyDao.insertStories(storyList)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ApiResponse<AddStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.addNewStory(token, file, description)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

}