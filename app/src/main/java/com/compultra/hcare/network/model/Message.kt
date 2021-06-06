package com.compultra.hcare.network.model

import com.google.gson.annotations.SerializedName

data class PlainMessageResponse(
    @SerializedName("message") val message: String
)

data class MessageResponse<T>(
        @SerializedName("message") val message: String,
        @SerializedName("data") val data: T
)

data class MessageArrayResponse<T>(
        @SerializedName("message") val message: String,
        @SerializedName("array") val array: List<T>
)