package com.compultra.hcare.network

import com.compultra.hcare.network.model.*
import com.compultra.hcare.util.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface PatientDataApi {

    @GET("get_doctors")
    suspend fun getAllDoctors(): MessageArrayResponse<EmailNameArea>

    @GET("consult/patient")
    suspend fun getConsultations(): MessageArrayResponse<Consultation>

    @FormUrlEncoded
    @POST("consult/patient")
    suspend fun postConsultation(
            @Field("doctorEmail") doctorEmail: String,
            @Field("requestedTimeBegin") requestedTimeBegin: String,
            @Field("requestedTimeEnd") requestedTimeEnd: String
    ): PlainMessageResponse

    @GET("chat")
    suspend fun getChats(): MessageArrayResponse<EmailName>

    @GET("chat/{email}")
    suspend fun getMessages(
            @Path("email") email: String
    ): MessageArrayResponse<ChatMessage>

    @FormUrlEncoded
    @POST("chat/{email}")
    suspend fun putMessage(
            @Path("email") email: String,
            @Field("content") content: String
    ): PlainMessageResponse

    companion object {
        private var token: String? = null
        private var _retrofitService: PatientDataApi? = null

        private val gsonConverter = GsonConverterFactory.create(
                GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create()
        )


        val retrofitService: PatientDataApi
            get() = _retrofitService ?: createNewInstance()

        fun setToken(token: String?) {
            if (this.token != token) {
                this.token = token
                createNewInstance()
            }
        }

        fun createNewInstance(): PatientDataApi {
            if (token == null) {
                this._retrofitService = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(gsonConverter)
                        .build()
                        .create(PatientDataApi::class.java)
            } else {
                val requestInterceptor = Interceptor {
                    val request = it
                            .request()
                            .newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build()
                    return@Interceptor it.proceed(request)
                }
                val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                        .addInterceptor(requestInterceptor)
                        .build()
                _retrofitService = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(gsonConverter)
                        .client(okHttpClient)
                        .build()
                        .create(PatientDataApi::class.java)
            }
            return _retrofitService!!
        }
    }
}