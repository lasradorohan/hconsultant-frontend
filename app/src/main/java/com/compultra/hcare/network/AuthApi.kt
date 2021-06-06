package com.compultra.hcare.network

import com.compultra.hcare.network.model.MessageResponse
import com.compultra.hcare.network.model.PlainMessageResponse
import com.compultra.hcare.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @FormUrlEncoded
    @POST("patient/register")
    suspend fun registerPatient(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("age") age: Int,
        @Field("gender") gender: String,
        @Field("weight") weight: Int,
        @Field("blood_group") blood: String
    ): PlainMessageResponse

    @FormUrlEncoded
    @POST("doctor/register")
    suspend fun registerDoctor(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("age") age: Int,
        @Field("gender") gender: String,
        @Field("area") area: String
    ): PlainMessageResponse

    @FormUrlEncoded
    @POST("patient/login")
    suspend fun loginPatient(
        @Field("email") email: String,
        @Field("password") password: String
    ): MessageResponse<String>



    @FormUrlEncoded
    @POST("doctor/login")
    suspend fun loginDoctor(
        @Field("email") email: String,
        @Field("password") password: String
    ): MessageResponse<String>

    @FormUrlEncoded
    @POST("checkAuth")
    suspend fun checkAuth(
            @Field("token") token: String
    ): PlainMessageResponse

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService: AuthApi by lazy {
            retrofit.create(AuthApi::class.java)
        }
    }
}

