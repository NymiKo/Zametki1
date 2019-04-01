package com.example.zametki1.RestAPIRegistration

import com.example.zametki1.RestAPILogin.Post
import com.google.gson.Gson
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface JSONPlaceHolderApiReg {
    @POST("/register.php")
    fun postDataReg(@Body bodyReg: RequestBody): Call<PostReg>
}

data class RegistrationRequestModel(val Login: String, val Password: String, val Name: String, val Surname:String, val NumberPhone: String, val Email:String){
    fun getBodyReg(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}