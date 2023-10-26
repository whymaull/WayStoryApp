package com.example.waystoryapp.data.api

sealed class ApiResponse<out R> {

    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
    data object Empty : ApiResponse<Nothing>()

}
