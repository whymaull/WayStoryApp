package com.example.waystoryapp.data.src

import com.example.waystoryapp.data.api.ApiResponse
import com.example.waystoryapp.data.remote.auth.AuthBody
import com.example.waystoryapp.data.remote.auth.AuthResponse
import com.example.waystoryapp.data.remote.auth.AuthService
import com.example.waystoryapp.data.remote.auth.LoginBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response

class AuthSrc constructor(private val authService: AuthService) {

    suspend fun registerUser(authBody: AuthBody): Flow<ApiResponse<Response<AuthResponse>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.registUser(authBody)
                if (response.code() == 201) {
                    emit(ApiResponse.Success(response))
                } else if (response.code() == 400) {
                    val errorBody = JSONObject(response.errorBody()!!.string())
                    emit(ApiResponse.Error(errorBody.getString("message")))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<AuthResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.loginUser(loginBody)
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