package com.example.waystoryapp.data.api

import com.example.waystoryapp.data.response.AddStoryResponse
import com.example.waystoryapp.data.response.DetailResponse
import com.example.waystoryapp.data.response.ResponseLogin
import com.example.waystoryapp.data.response.ResponseRegister
import com.example.waystoryapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>

    @GET("stories")
    suspend  fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse

    @GET("stories?location=1")
    fun getStoryWithLocation(
        @Header("Authorization") token: String,
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<DetailResponse>

}