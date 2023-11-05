package com.example.waystoryapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.data.response.StoryResponse
import com.example.waystoryapp.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class MainViewModel (private val reps: UserRepository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val qute: LiveData<PagingData<Entities>> =
        reps.getQuote().cachedIn(viewModelScope)


//    fun fetchListStories(token:String) {
//        Log.i("fetchListStories", "$token")
//        _isLoading.postValue(true)
//        val client = ApiConfig.getApiService().getStory("Bearer $token")
//
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//
//                call: Call<StoryResponse>,
//                response: retrofit2.Response<StoryResponse>,
//            ) {
//                Log.i("AddStoryViewModel", "${response.code()}")
//
//                if (response.isSuccessful) {
//                    Log.i("AddStoryViewModel", "Berhasil")
//
//                    val appResponse = response.body()
//                    val itemsList = appResponse?.listStory ?: emptyList()
//                    _listStories.postValue(itemsList as List<ListStoryItem>?)
//                    Log.i(
//                        "AddStoryViewModel", "${appResponse}"
//                    )
//                    _isLoading.value = false
//
//                } else {
//
//                    _isLoading.value = false
//                }
//
//                _isLoading.postValue(false)
//            }
//
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                Log.e("AddStoryViewModel", "Gagal daftar: ${t.message}")
//                _isLoading.postValue(false)
//            }
//        })
//
//    }

    fun getSession(): LiveData<UserModel> {
        return reps.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            reps.logout()
        }
    }
}