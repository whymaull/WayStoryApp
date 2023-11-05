package com.example.waystoryapp.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.waystoryapp.data.reps.UserRepository
import com.example.waystoryapp.pref.UserModel
import kotlinx.coroutines.launch

class SplashViewModel(private val reps: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return reps.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            reps.logout()
        }
    }
}