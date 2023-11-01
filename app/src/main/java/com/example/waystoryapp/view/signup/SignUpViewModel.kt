package com.example.waystoryapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waystoryapp.data.api.ApiConfig
import com.example.waystoryapp.data.response.ResponseRegister
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isMessage = MutableLiveData<String>()
    val isMessage: LiveData<String> = _isMessage


    fun registerUser(name:String,email:String,password:String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().signup(name, email, password)

        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(

                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    val appResponse = response.body()
                    Log.i("SignupViewModel", "${appResponse}")
                    _isMessage.value = appResponse?.message!!

                } else {
                    val str = response.errorBody()!!.string()
                    try {
                        val json = JSONObject(str)

                        _isMessage.value = json.getString("message")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

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