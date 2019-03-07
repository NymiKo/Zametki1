package com.example.zametki1.RestAPI

import retrofit2.Call
import retrofit2.http.*

interface JSONPlaceHolderApi {
    @POST("/login.php")
    fun postData(@Field("Login") Login: String,
                 @Field("Password") Password: String): Call<Post>
}