package com.example.waystoryapp.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("register")
    suspend fun registUser(
        @Body authBody: AuthBody
    ): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(
        @Body loginBody: LoginBody
    ): AuthResponse

}