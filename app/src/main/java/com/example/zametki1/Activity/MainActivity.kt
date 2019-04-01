package com.example.zametki1.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.example.zametki1.Fragment.ProfileFragment
import kotlin.concurrent.thread
import com.example.zametki1.databinding.ActivityMainBinding
import com.example.zametki1.R
import com.example.zametki1.RestAPILogin.LoginRequestModel
import com.example.zametki1.RestAPILogin.Post
import com.example.zametki1.RestAPILogin.NetworkService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        myPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)

        binding.buttonSignOn.setOnClickListener{
            sendGet()
        }
        binding.buttonSignIn.setOnClickListener{
            startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
        }
    }

    private fun sendGet() {
        if(binding.editLogin.length() >= 3) {
            binding.wrongLogin.visibility = View.GONE
            Log.e("OKHTTP3", "функция POST вызвана")
            autorization(binding.editLogin.text.toString(), binding.editPassword.text.toString())
        } else{
            binding.wrongLogin.visibility = View.VISIBLE
        }
    }

    fun autorization(Login: String, Password: String){
        thread {
            NetworkService.getInstance()
                .getJSONApi()
                .postData(
                    LoginRequestModel(Login, Password).getBody())
                .enqueue(object : Callback<Post> {
                    @Override
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: Post? = response.body()
                        Log.e("OKHTTP3", post?.serverAnswer.toString())
                        if (post?.serverAnswer.toString() != "false"){
                            getID(post?.serverAnswer)
                            editor = myPreferences.edit()
                            editor.putString("Login", Login)
                            editor.putString("Password", Password)
                            editor.apply()
                            startActivity(Intent(this@MainActivity, Tasks::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                        else
                            Toast.makeText(
                                applicationContext, "Логин или пароль введены неверно!",
                                Toast.LENGTH_SHORT
                            ).show()
                    }

                    @Override
                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        Log.e("OKHTTP3", "Что-то пошло не так...")
                        t.printStackTrace()
                        Toast.makeText(
                            applicationContext, "Ошибка!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Looper.loop()
                    }
                })
        }
    }

    fun getID(id: Int?): ProfileFragment{
        val idUser = Bundle()
        idUser.putInt("id", id!!)
        ProfileFragment().arguments = idUser
        return ProfileFragment()
    }
}
