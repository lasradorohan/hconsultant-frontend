package com.compultra.hcare.ui.chat

import androidx.lifecycle.*
import com.compultra.hcare.network.DoctorDataApi
import com.compultra.hcare.network.PatientDataApi
import com.compultra.hcare.network.model.ChatMessage
import com.compultra.hcare.network.model.MessageArrayResponse
import kotlinx.coroutines.launch

class ChatViewModel() : ViewModel() {
    private var _userType = ""
    private var _recepientEmail = ""
    private var _recepientName = ""
    val userType get() = _userType
    val recepientEmail get() = _recepientEmail
    val recepientName get() = _recepientName

    private var _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages
//
//    init {
//        fetchMessages()
//    }

    //TODO: use hilt injection instead
    fun refreshParams(type: String?, email: String?, name: String?){
        type?.let{ _userType = it }
        email?.let{ _recepientEmail = it }
        name?.let{ _recepientName = it }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            when(_userType){
                "patient" -> PatientDataApi.retrofitService.putMessage(_recepientEmail, message)
                "doctor" -> DoctorDataApi.retrofitService.putMessage(_recepientEmail, message)
            }
            fetchMessages()
        }
    }

    fun fetchMessages(){
        viewModelScope.launch {
            var response: MessageArrayResponse<ChatMessage>? = null
            when(_userType){
                "patient" -> response = PatientDataApi.retrofitService.getMessages(_recepientEmail)
                "doctor" -> response = DoctorDataApi.retrofitService.getMessages(_recepientEmail)
            }
            if (response?.message == "successful"){
                _messages.value = response.array
            }

        }
    }

    fun isError(): Boolean{
        return _userType == "" || _recepientEmail == "" || _recepientName == ""
    }

    fun logStr(): String {
        return "$_userType, $_recepientEmail, $_recepientName"
    }
}