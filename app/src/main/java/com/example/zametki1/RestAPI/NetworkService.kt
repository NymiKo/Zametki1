package com.example.zametki1.RestAPI

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {
    private lateinit var mInstance: NetworkService
    val BASE_URL: String = "http://android-test-php.000webhostapp.com"
    private lateinit var retrofit: Retrofit

    private fun NetworkService(){
        val client = OkHttpClient.Builder()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }

    fun getInstance(): NetworkService {
        if(mInstance == null){
            mInstance = NetworkService() as NetworkService
        }
        return mInstance
    }

    fun getJSONApi(): JSONPlaceHolderApi{
        return retrofit.create(JSONPlaceHolderApi::class.java)
    }
}