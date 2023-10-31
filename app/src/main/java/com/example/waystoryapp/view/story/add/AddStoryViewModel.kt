package com.example.waystoryapp.view.story.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.response.AddStoryResponse
import com.example.waystoryapp.pref.UserModel
import okhttp3.MultipartBody
import retrofit2.Call

class AddStoryViewModel (private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addStory(token: String, file: MultipartBody.Part, description : String ){
        _isLoading.value = true
        val client = ApiConfig.getApiService().postStory("Bearer $token",file, description)
        Log.i("AddStoryViewModel", "AddStoryViewModel: ${token} ")

        client.enqueue(object : retrofit2.Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: retrofit2.Response<AddStoryResponse>,
            ) {
                Log.i("AddStoryViewModel", "${response.code()}")

                if (response.isSuccessful) {
                    Log.i("AddStoryViewModel", "Berhasil")

                    val appResponse = response.body()
                    Log.i(
                        "AddStoryViewModel", "${appResponse}"
                    )
                    _isSuccess.value = true
                    _isLoading.value = false

                } else {
                    _isLoading.value = false
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.e("AddStoryViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })

    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}