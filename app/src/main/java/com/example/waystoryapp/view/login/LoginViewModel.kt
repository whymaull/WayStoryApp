package com.example.waystoryapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waystoryapp.data.UserRepository
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.response.ResponseLogin
import com.example.waystoryapp.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val reps: UserRepository) : ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            reps.saveSession(user)
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun signIn(email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().signIn(email, password)

        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(

                call: Call<ResponseLogin>,
                response: Response<ResponseLogin>,
            ) {
                Log.i("SignupViewModel", "${response.code()}")

                if (response.isSuccessful) {
                    Log.i("SignupViewModel", "Berhasil")

                    val appResponse = response.body()
                    saveSession(UserModel(token = appResponse?.loginResult?.token!!, isLogin = true))
                    Log.i(
                        "SignupViewModel", "${appResponse}"
                    )
                    _isSuccess.value = true
                    _isLoading.value = false

                } else {


                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("SignupViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })
    }

}