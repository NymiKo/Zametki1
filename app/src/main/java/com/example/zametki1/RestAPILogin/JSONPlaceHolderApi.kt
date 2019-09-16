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

    @POST("/task.php")
    fun postCreateTask(@Body bodyCreateTask: RequestBody): Call<PostCreateTask>

    @POST("/show_task.php")
    fun postShowTask(@Body bodyShowTask: RequestBody): Call<List<PostShowTask>>

    @POST("/task_view.php")
    fun postTaskView(@Body bodyTaskView: RequestBody): Call<PostTaskView>

    @POST("/task_delete.php")
    fun postTaskDelete(@Body bodyTaskDelete: RequestBody): Call<PostTaskDelete>

    @POST("/task_edit.php")
    fun  postTaskEdit(@Body bodyTaskEdit: RequestBody): Call<PostTaskEdit>
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

data class CreateTaskModel(val Name: String, val Discription: String, val Color: Int, val Creator: Int, val Participant: String){
    fun getBody(): RequestBody {
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class ShowTaskModel(val Id: Int){
    fun getBody(): RequestBody{
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class TaskViewModel(val IdTask: Int){
    fun getBody(): RequestBody{
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class TaskDeleteModel(val IdCreator: Int, val TaskName: String, val TaskDescription: String){
    fun getBody(): RequestBody{
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}

data class TaskEditModel(val CreatorId: Int, val OldTaskName: String, val OldTaskDescription: String, val TaskName: String, val TaskDescription: String){
    fun getBody(): RequestBody{
        val gson = Gson()
        return RequestBody.create(null, gson.toJson(this))
    }
}
//class getBody{
//
//}