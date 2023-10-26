package com.example.waystoryapp.data.reps

import com.example.waystoryapp.data.api.ApiResponse
import com.example.waystoryapp.data.remote.story.AddStoryResponse
import com.example.waystoryapp.data.remote.story.GetStoryResponse
import com.example.waystoryapp.data.src.StorySrc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryReps constructor(private val storySrc: StorySrc) {

    suspend fun getAllStories(token: String): Flow<ApiResponse<GetStoryResponse>> {
        return storySrc.getAllStories(token).flowOn(Dispatchers.IO)
    }

    suspend fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ApiResponse<AddStoryResponse>> {
        return storySrc.addNewStory(token, file, description)
    }

}