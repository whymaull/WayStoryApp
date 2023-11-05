package com.example.waystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.waystoryapp.data.reps.UserRepository
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.response.ListStoryItem
import com.example.waystoryapp.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel (private val reps: UserRepository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val storyPage: LiveData<PagingData<Entities>> =
        reps.getQuote().cachedIn(viewModelScope)


    fun getSession(): LiveData<UserModel> {
        return reps.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            reps.logout()
        }
    }
}