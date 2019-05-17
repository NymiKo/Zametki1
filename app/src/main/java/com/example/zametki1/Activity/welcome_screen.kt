package com.example.zametki1.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.zametki1.R
import com.example.zametki1.RestAPILogin.LoginRequestModel
import com.example.zametki1.RestAPILogin.NetworkService
import com.example.zametki1.RestAPILogin.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class welcome_screen : AppCompatActivity() {
    val welcomeTimeOut: Long = 2000
    private lateinit var myPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        myPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val loginPreferences = myPreferences.getString("Login", "null")
        val passwordPreferences = myPreferences.getString("Password", "null")
        if(loginPreferences != "null"){
            autorization(loginPreferences, passwordPreferences)
        }
        else{
            Handler().postDelayed(Runnable(){
                startActivity(Intent(this@welcome_screen, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }, welcomeTimeOut)
        }
    }

    fun autorization(Login: String, Password: String){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postDataLogin(
                    LoginRequestModel(Login, Password).getBody())
                .enqueue(object : Callback<Post> {
                    @Override
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: Post? = response.body()
                        Log.e("OKHTTP3", post?.serverAnswerId.toString())
                        if (post?.serverAnswerId.toString() != "false"){
                            Handler().postDelayed(Runnable() {
                                val intent = Intent(this@welcome_screen, Tasks::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.putExtra("id", post?.serverAnswerId)
                                intent.putExtra("name", post?.serverAnswerName)
                                intent.putExtra("email", post?.serverAnswerEmail)
                                startActivity(intent)
                            }, welcomeTimeOut)
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        Log.e("OKHTTP3", "Что-то пошло не так...")
                        t.printStackTrace()
                        Toast.makeText(
                            applicationContext, "Нет подключения к интернету!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Looper.loop()
                    }
                })
        }
    }
}
