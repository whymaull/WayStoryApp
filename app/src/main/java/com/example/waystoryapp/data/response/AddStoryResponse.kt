package com.example.waystoryapp.data.response

public class AddStoryResponse {

    private val error = false
    private val message: String? = null

    fun isError(): Boolean {
        return error
    }

    fun getMessage(): String? {
        return message
    }

}