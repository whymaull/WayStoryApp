package com.example.waystoryapp.data.response

import com.google.gson.annotations.SerializedName

public class AddStoryResponse {

    @field:SerializedName("error")
    val error: Boolean? = null

    @field:SerializedName("message")
    val message: String? = null
}