package com.example.zametki1.RestAPI

import com.google.gson.Gson
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface JSONPlaceHolderApi {
    @POST("/login.php")
    fun postData(@Body body: RequestBody): Call<Post>
}

data class LoginRequestModel(val Login: String, val Password: String){
    fun getBody():RequestBody{
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}