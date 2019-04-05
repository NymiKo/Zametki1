package com.example.zametki1.RestAPILogin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(@SerializedName("answer")
                @Expose
                val serverAnswerLogin: Int?)

data class PostReg(@SerializedName("answer")
                   @Expose
                   val serverAnswerReg: String?)