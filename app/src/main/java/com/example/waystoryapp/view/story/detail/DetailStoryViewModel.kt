package com.example.waystoryapp.view.story.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.response.DetailResponse
import com.example.waystoryapp.data.response.Story
import com.example.waystoryapp.pref.UserModel
import retrofit2.Call
import retrofit2.Callback

class DetailStoryViewModel(private val reps: UserRepository) : ViewModel() {

    private val _getStories = MutableLiveData<Story>()
    val getStories: LiveData<Story> = _getStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStory(token: String, id: String) {

        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getDetailStory( "Bearer $token",id)

        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: retrofit2.Response<DetailResponse>,
            ) {
                Log.i("AddStoryViewModel", "${response.code()}")

                if (response.isSuccessful) {

                    val appResponse = response.body()?.story
                    val itemsList = appResponse
                    _getStories.postValue(itemsList!!)
                    Log.i(
                        "AddStoryViewModel", " AddStoryViewModel :${appResponse}"
                    )
                    _isLoading.value = false

                } else {

                    _isLoading.value = false
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("AddStoryViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })

    }

    fun getSession(): LiveData<UserModel> {
        return reps.getSession().asLiveData()
    }

}