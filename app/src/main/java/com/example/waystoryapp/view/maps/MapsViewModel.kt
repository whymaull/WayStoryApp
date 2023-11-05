package com.example.waystoryapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.data.response.StoryResponse
import com.example.waystoryapp.pref.UserModel
import retrofit2.Call
import retrofit2.Callback

class MapsViewModel (private val reps: UserRepository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun fetchStoryLocation(token : String) {
        Log.i("fetchListStories", "")
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getStoryWithLocation("Bearer $token")

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(

                call: Call<StoryResponse>,
                response: retrofit2.Response<StoryResponse>,
            ) {
                Log.i("AddStoryLocation", "${response.code()}")

                if (response.isSuccessful) {
                    Log.i("AddStoryLocation", "Berhasil")

                    val locResponse = response.body()
                    val itemsList = locResponse?.listStory ?: emptyList()
                    _listStories.postValue(itemsList as List<ListStoryItem>?)
                    Log.i(
                        "AddStoryLoaction", "${locResponse}"
                    )
                    _isLoading.value = false

                } else {

                    _isLoading.value = false
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("AddStoryLoaction", "Gagal Mendapatkan Lokasi: ${t.message}")
                _isLoading.postValue(false)
            }
        })

    }

    fun getSession(): LiveData<UserModel> {
        return reps.getSession().asLiveData()
    }

}