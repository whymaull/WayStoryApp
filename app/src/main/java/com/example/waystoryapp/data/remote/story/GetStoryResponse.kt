package com.example.waystoryapp.data.remote.story

import com.example.waystoryapp.data.datamodel.Story
import com.google.gson.annotations.SerializedName

data class GetStoryResponse(

    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val listStory: List<Story>

)
