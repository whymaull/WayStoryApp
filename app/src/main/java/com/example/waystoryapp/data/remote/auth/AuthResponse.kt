package com.example.waystoryapp.data.remote.auth

import com.example.waystoryapp.data.datamodel.User
import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val loginResult: User

)
