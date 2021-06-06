package com.compultra.hcare.ui.patient.doctor

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.compultra.hcare.network.PatientDataApi
import com.compultra.hcare.network.model.EmailName
import com.compultra.hcare.network.model.EmailNameArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorPatientViewModel(application: Application) : AndroidViewModel(application) {
    private val _allDoctors = MutableLiveData<List<EmailNameArea>>()
    val allDoctors: LiveData<List<EmailNameArea>> = _allDoctors

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        retrieveDoctors()
    }

    fun retrieveDoctors(){
        viewModelScope.launch {
            _isLoading.value = true
            val response = PatientDataApi.retrofitService.getAllDoctors()
            Log.d("my_response", response.toString())
            _allDoctors.value = response.array
            _isLoading.value = false
        }
    }




//    fun requestConsultation(docEmail: String, from: String, to: String){
//        viewModelScope.launch {
//
//        }
//    }
}