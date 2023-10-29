package com.example.waystoryapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.response.ResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun registerUser(name:String,email:String,password:String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().signup(name, email, password)

        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(

                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                Log.i("SignupViewModel", "${response.code()}")

                if (response.isSuccessful) {
                    Log.i("SignupViewModel", "Berhasil")

                    val appResponse = response.body()
                    Log.i("SignupViewModel", "${appResponse}")
//               val itemsList = githubResponse?.items ?: emptyList()

                } else {


                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e("SignupViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })
    }

}