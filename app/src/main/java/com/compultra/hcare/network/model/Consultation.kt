package com.compultra.hcare.network.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Consultation(
    @SerializedName("consultationID") val consultationID: Int,
    @SerializedName("status") val status: String,
    @SerializedName("requestedTimeBegin") val requestedTimeBegin: Date,
    @SerializedName("requestedTimeEnd") val requestedTimeEnd: Date,
    @SerializedName("scheduledTimeBegin") val scheduledTimeBegin: Date?,
    @SerializedName("scheduledTimeEnd") val scheduledTimeEnd: Date?,
    @SerializedName("prescription") val prescription: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String
)