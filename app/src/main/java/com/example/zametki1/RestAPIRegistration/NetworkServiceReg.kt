package com.example.zametki1.RestAPIRegistration

import com.example.zametki1.RestAPILogin.JSONPlaceHolderApi
import com.example.zametki1.RestAPILogin.NetworkService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkServiceReg {

    companion object {
        private const val BASE_URL: String = "http://android-test-php.000webhostapp.com"

        fun getInstanceReg(): NetworkServiceReg =
            NetworkServiceReg()
    }

    private var retrofit: Retrofit

    init{
        val client = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        client.addInterceptor(logging)

        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()
    }

    fun getJSONApiReg(): JSONPlaceHolderApiReg {
        return retrofit.create(JSONPlaceHolderApiReg::class.java)
    }
}