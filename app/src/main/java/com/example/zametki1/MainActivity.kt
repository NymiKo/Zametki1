package com.example.zametki1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

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
        try{
            var editLog = findViewById<EditText>(R.id.edit_login)
            var editPass = findViewById<EditText>(R.id.edit_password)

            val client: OkHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()
                .add("Login", editLog.toString())
                .add("Пароль", editPass.toString())
                .build()

            val request: Request = Request.Builder()
                .url("android-test-php.000webhostapp.com/login.php")
                .post(formBody)
                .build()

            try{
                val response: Response = client.newCall(request).execute()

                val serverAnswer: String = response.body()!!.string()
                Toast.makeText(applicationContext, serverAnswer, Toast.LENGTH_SHORT).show()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
        catch (e: IOException){
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
