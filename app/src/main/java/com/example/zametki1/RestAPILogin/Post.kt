package com.example.zametki1.RestAPILogin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(@SerializedName("Id")
                                                                                                  @Expose
                                                                                                  val serverAnswerId: Int?,
                                                                        @SerializedName("Name")
                                                                                                  @Expose
                                                                                                  val serverAnswerName: String?,
                                                                        @SerializedName("Email")
                                                                                                  @Expose
                                                                                                  val serverAnswerEmail: String?,
                                                                        @SerializedName("answer")
                                                                                                  @Expose
                                                                                                  val serverAnswer: String?)

data class PostReg(@SerializedName("answer")
                   @Expose
                   val serverAnswerReg: String?)

data class PostViewProfile(@SerializedName("Name")
                           @Expose
                           val serverAnswerName: String?,
                           @SerializedName("Surname")
                           @Expose
                           val serverAnswerSurname: String?,
                           @SerializedName("NumberPhone")
                           @Expose
                           val serverAnswerNumberPhone: String?,
                           @SerializedName("Email")
                           @Expose
                           val serverAnswerEmail: String?)

data class PostEditProfile(@SerializedName("answer")
                           @Expose
                           val serverAnswerEditProfile: String?)

data class PostCreateTask(@SerializedName("answer")
                          @Expose
                          val serverAnswerCreateTask: String?)

data class PostShowTask(@SerializedName("TaskName")
                        @Expose
                        val serverAnswerTaskName: String?,
                        @SerializedName("TaskColor")
                        @Expose
                        val serverAnswerTaskColor: Int,
                        @SerializedName("IdTask")
                        @Expose
                        val serverAnswerTaskId: Int,
                        @SerializedName("answer")
                        @Expose
                        val serverAnswerTask: String?)

data class PostTaskView(@SerializedName("TaskDescription")
                        @Expose
                        val serverAnswerTaskDescription: String?,
                        @SerializedName("IdCreator")
                        @Expose
                        val serverAnswerTaskIdCreator: Int,
                        @SerializedName("answer")
                        @Expose
                        val serverAnswerTask: String?)

data class PostTaskDelete(@SerializedName("answer")
                          @Expose
                          val serverAnswerTask: String?)

data class PostTaskEdit(@SerializedName("answer")
                        @Expose
                        val serverAnswerTask: String?)