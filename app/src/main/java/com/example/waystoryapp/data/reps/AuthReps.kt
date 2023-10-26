package com.example.waystoryapp.data.reps

import com.example.waystoryapp.data.api.ApiResponse
import com.example.waystoryapp.data.remote.auth.AuthBody
import com.example.waystoryapp.data.remote.auth.AuthResponse
import com.example.waystoryapp.data.remote.auth.LoginBody
import com.example.waystoryapp.data.src.AuthSrc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class AuthReps constructor(private val authDataSrc: AuthSrc) {

    suspend fun registerUser(authBody: AuthBody): Flow<ApiResponse<Response<AuthResponse>>> {
        return authDataSrc.registerUser(authBody).flowOn(Dispatchers.IO)
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<AuthResponse>> {
        return authDataSrc.loginUser(loginBody).flowOn(Dispatchers.IO)
    }

}