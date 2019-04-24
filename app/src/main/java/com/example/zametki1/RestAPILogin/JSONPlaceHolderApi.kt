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

    @POST("/profile.php")
    fun postViewProfile(@Body bodyViewProfile: RequestBody): Call<PostViewProfile>

    @POST("/profile_edit.php")
    fun postEditProfile(@Body bodyEditProfile: RequestBody): Call<PostEditProfile>
}

data class LoginRequestModel(val Login: String, val Password: String){
    fun getBody(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class RegistrationRequestModel(val Login: String, val Password: String, val Name: String, val Surname:String, val NumberPhone: String, val Email:String){
    fun getBody(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class ProfileViewModel(val id: Int){
    fun getBody(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class ProfileEditModel(val Id: Int, val Name: String, val Surname:String, val NumberPhone: String, val Email:String){
    fun getBody(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

//class getBody{
//
//}