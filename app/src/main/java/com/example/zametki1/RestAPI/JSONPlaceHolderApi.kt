package com.example.zametki1.RestAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface JSONPlaceHolderApi {
    @POST("/login.php")
    fun postData(@Body Login: String, Password: String): Call<Post>
}