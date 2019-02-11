package com.example.zametki1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import android.R.string
import android.os.AsyncTask.execute
import android.widget.TextView
import okhttp3.FormBody
import okhttp3.RequestBody
import okhttp3.OkHttpClient



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.button_sign_on)
        btnLogin.setOnClickListener{
            sendGet()
        }
    }

    private fun sendGet() {
        val editLog = findViewById<EditText>(R.id.edit_login)
        val editPass = findViewById<EditText>(R.id.edit_password)

        if(editLog.length() >= 3) {
            Log.e("OKHTTP3", "функция POST вызвана")
            val client: OkHttpClient = OkHttpClient()

            //параметры запроса
            val formBody = FormBody.Builder()
                .add("Login", editLog.text.toString())
                .add("Password", editPass.text.toString())
                .build()
            Log.e("OKHTTP3", "Параметры указаны")

            //создание запроса
            val request = Request.Builder()
                .url("http://android-test-php.000webhostapp.com/login.php")
                .post(formBody)
                .build()
            Log.e("OKHTTP3", "Запрос создан")

            //исполняение запроса
            try {
                val response: Response = client.newCall(request).execute()//на этой строке ошибка

                val serverAnswer: String = response.body().toString()
                Log.e("OKHTTP3", "Ответ сервера: $serverAnswer")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else{
            val wrongLogin = findViewById<TextView>(R.id.wrong_login)
            wrongLogin.visibility = View.VISIBLE
        }
    }
}
