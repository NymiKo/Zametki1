package com.example.zametki1.RestAPI

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Post {
    @SerializedName("Ответ сервера:")
    @Expose
    private var serverAnswer: String? = null

    fun getServerAnswer(): String?{
        return serverAnswer
    }

    fun setServerAnswer(serverAnswer: String){
        this.serverAnswer = serverAnswer
    }
}