package com.example.zametki1.Activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.annotation.NonNull
import android.util.Log
import android.view.View
import okhttp3.*
import android.widget.Toast
import okhttp3.FormBody
import okhttp3.OkHttpClient
import kotlin.concurrent.thread
import com.example.zametki1.databinding.ActivityMainBinding
import com.example.zametki1.R
import com.example.zametki1.RestAPI.Post
import com.example.zametki1.RestAPI.NetworkService






class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.buttonSignOn.setOnClickListener{
            sendGet()
        }
    }

    private fun sendGet() {
        if(binding.editLogin.length() >= 3) {
            binding.wrongLogin.visibility = View.GONE
            Log.e("OKHTTP3", "функция POST вызвана")
            thread {
                NetworkService().getInstance()
                    .getJSONApi()
                    .postData(binding.editLogin.text.toString(), binding.editPassword.text.toString())
                    .enqueue(object : Callback<Post>() {
                        @Override
                        fun onResponse(@NonNull call: Call<Post>, @NonNull response: Response<Post>) {
                            val post: Post = response.body()
                            when (post.getServerAnswer()) {
                                "true" -> {
                                    val contactsIntent = Intent(this@MainActivity, Contacts::class.java)
                                    startActivity(contactsIntent)
                                }
                                "false" -> {
                                    Looper.prepare()
                                    Toast.makeText(
                                        applicationContext, "Логин или пароль введены неверно!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Looper.loop()
                                }
                            }
                        }

                        @Override
                        fun onFailure(@NonNull call: Call<Post>, @NonNull t: Throwable) {
                            Looper.prepare()
                            Toast.makeText(
                                applicationContext, "Ошибка!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Looper.loop()
                        }
                    })
            }

//            Log.e("OKHTTP3", "функция POST вызвана")
//            val client = OkHttpClient()
//
//            //параметры запроса
//            val formBody = FormBody.Builder()
//                .add("Login", binding.editLogin.text.toString())
//                .add("Password", binding.editPassword.text.toString())
//                .build()
//            Log.e("OKHTTP3", "Параметры указаны")
//
//            //создание запроса
//            val request = Request.Builder()
//                .url("http://android-test-php.000webhostapp.com/login.php")
//                .post(formBody)
//                .build()
//            Log.e("OKHTTP3", "Запрос создан")
//
//            //исполняение запроса
//            try {
//                thread {
//                    Log.e("OKHTTP3", "Запрос выполнен")
//                    val response: Response = client.newCall(request).execute()
//
//                    val serverAnswer = response.body()!!.string()
//
//                    when(serverAnswer){
//                        "true" -> {
//                            val contactsIntent = Intent(this@MainActivity, Contacts::class.java)
//                            startActivity(contactsIntent)
//                        }
//                        "false" -> {
//                            Looper.prepare()
//                            Toast.makeText(applicationContext, "Логин или пароль введены неверно!",
//                                           Toast.LENGTH_SHORT).show()
//                            Looper.loop()
//                        }
//                    }
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//            }
        } else{
            binding.wrongLogin.visibility = View.VISIBLE
        }
    }
}
