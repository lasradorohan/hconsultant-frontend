package com.compultra.hcare.network.model

import com.google.gson.annotations.SerializedName

data class EmailName (
        @SerializedName("email") val email: String,
        @SerializedName("name") val name: String
)

data class EmailNameArea (
        @SerializedName("email") val email: String,
        @SerializedName("name") val name: String,
        @SerializedName("area") val area: String
)