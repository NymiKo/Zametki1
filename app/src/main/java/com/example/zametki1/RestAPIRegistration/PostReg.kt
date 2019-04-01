package com.example.zametki1.RestAPIRegistration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostReg(@SerializedName("answer")
                   @Expose
                   val serverRegAnswer: String?)