package com.compultra.hcare.ui.doctor.consultation

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.compultra.hcare.network.DoctorDataApi
import com.compultra.hcare.network.model.Consultation
import kotlinx.coroutines.launch

class ConsultationDoctorViewModel(application: Application) : AndroidViewModel(application) {
    private val _consultations = MutableLiveData<List<Consultation>>()
    val consultations: LiveData<List<Consultation>> = _consultations

    private var _message: String = ""
    val message: String get(){
        _showMessage.value = false
        return _message
    }
    private val _showMessage: MutableLiveData<Boolean> = MutableLiveData(false)
    val showMessage: LiveData<Boolean> = _showMessage
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        retrieveConsultations()
    }

    fun retrieveConsultations() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = DoctorDataApi.retrofitService.getConsultations()
            _consultations.value = response.array
            _isLoading.value = false
        }
    }

    fun cancelConsult(consultationID: Int) {
        viewModelScope.launch {
            DoctorDataApi.retrofitService.reviewConsultation(consultationID, "cancel", "", "")
            retrieveConsultations()
        }
    }

    fun scheduleConsult(consultationID: Int, fromTime: String, toTime: String) {
//        Log.d("scheduleConsult", "$consultationID, $fromTime, $toTime")
        viewModelScope.launch {
            DoctorDataApi.retrofitService.reviewConsultation(
                    consultationID = consultationID,
                    status = "approve",
                    scheduledTimeBegin = fromTime,
                    scheduledTimeEnd = toTime
            )
            retrieveConsultations()
        }
    }

    fun setPrescription(consultationID: Int, prescription: String? = null) {
        viewModelScope.launch {
            _message = DoctorDataApi.retrofitService
                    .postPrescription(consultationID, prescription ?: "").message
            _showMessage.value = true
        }
        retrieveConsultations()
    }
}