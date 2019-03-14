package com.example.zametki1.RestAPI

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {

    companion object {
        private const val BASE_URL: String = "http://android-test-php.000webhostapp.com"

        fun getInstance(): NetworkService = NetworkService()
    }

    private var retrofit: Retrofit

    init{
        val client = OkHttpClient.Builder()

        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()
    }

    fun getJSONApi(): JSONPlaceHolderApi{
        return retrofit.create(JSONPlaceHolderApi::class.java)
    }
}