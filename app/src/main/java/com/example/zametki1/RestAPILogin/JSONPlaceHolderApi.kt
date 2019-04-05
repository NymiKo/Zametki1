package com.example.zametki1.RestAPILogin

import com.google.gson.Gson
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface JSONPlaceHolderApi {
    @POST("/login.php")
    fun postDataLogin(@Body body: RequestBody): Call<Post>

    @POST("/register.php")
    fun postDataReg(@Body bodyReg: RequestBody): Call<PostReg>
}

data class LoginRequestModel(val Login: String, val Password: String){
    fun getBody():RequestBody{
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class RegistrationRequestModel(val Login: String, val Password: String, val Name: String, val Surname:String, val NumberPhone: String, val Email:String){
    fun getBodyReg(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}