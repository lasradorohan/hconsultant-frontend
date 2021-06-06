package com.compultra.hcare.ui.chat

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.compultra.hcare.network.DoctorDataApi
import com.compultra.hcare.network.PatientDataApi
import com.compultra.hcare.network.model.EmailName
import com.compultra.hcare.util.MY_SHARED_PREFS
import com.compultra.hcare.util.USER_TYPE
import kotlinx.coroutines.launch

class ChatListViewModel(application: Application) : AndroidViewModel(application) {

    private var userType: String =
        application.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(USER_TYPE, null).toString()

    private val _chats = MutableLiveData<List<EmailName>>()
    val chats: LiveData<List<EmailName>> = _chats

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        retrieveChats()
    }

    fun retrieveChats() {
        viewModelScope.launch {
            _isLoading.value = true
            when(userType){
                "patient" -> {
                    val response = PatientDataApi.retrofitService.getChats()
                    _chats.value = response.array
                }
                "doctor" -> {
                    val response = DoctorDataApi.retrofitService.getChats()
                    _chats.value = response.array
                }
            }
            _isLoading.value = false
        }
    }
}