package com.example.waystoryapp.data.api

import com.example.waystoryapp.data.response.AddStoryResponse
import com.example.waystoryapp.data.response.DetailResponse
import com.example.waystoryapp.data.response.ResponseLogin
import com.example.waystoryapp.data.response.ResponseRegister
import com.example.waystoryapp.data.response.StoryResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<ResponseLogin>

    @POST("stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: String,
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getStory(@Header("Authorization")token : String
    ): Call<StoryResponse>
    @GET("stories/{id}")
    fun getDetailStory(@Path("id") id: String): Call<DetailResponse>

}