package com.compultra.hcare.ui.patient.consultation

import android.app.Application
import androidx.lifecycle.*
import com.compultra.hcare.network.PatientDataApi
import com.compultra.hcare.network.model.Consultation
import kotlinx.coroutines.launch

class ConsultationPatientViewModel(application: Application) : AndroidViewModel(application) {
    private val _consultations = MutableLiveData<List<Consultation>>()
    val consultations: LiveData<List<Consultation>> = _consultations

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        retrieveConsultations()
    }

    fun retrieveConsultations(){
        viewModelScope.launch {
            _isLoading.value = true
            val response = PatientDataApi.retrofitService.getConsultations()
            _consultations.value = response.array
            _isLoading.value = false
        }
    }
}