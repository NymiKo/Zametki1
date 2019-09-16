package com.example.zametki1.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
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

        //Заголовок экрана
        title = "Авторизация"

        //Получение сохраненных настроек приложения
        myPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)

        //Вызов метода для авторизации
        binding.buttonSignOn.setOnClickListener{
            sendGet()
        }

        //Вызов метода для перехода на экран регистрации
        binding.buttonSignIn.setOnClickListener{
            startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
        }
    }

    //Метод для проверки данных и авторизации пользователя
    private fun sendGet() {
        if(binding.editLogin.length() >= 3) {
            binding.wrongLogin.visibility = View.GONE
            Log.e("OKHTTP3", "функция POST вызвана")
            autorization(binding.editLogin.text.toString(), binding.editPassword.text.toString())
        } else{
            binding.wrongLogin.visibility = View.VISIBLE
        }
    }

    //Метод для получение ответа сервера при авторизации
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
                        when(post?.serverAnswer.toString()) {
                            "true" -> {
                                getID(post?.serverAnswerId)
                                editor = myPreferences.edit()
                                editor.putString("Login", Login)
                                editor.putString("Password", Password)
                                editor.apply()
                                val intent = Intent(this@MainActivity, Tasks::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.putExtra("id", post?.serverAnswerId)
                                intent.putExtra("name", post?.serverAnswerName)
                                intent.putExtra("email", post?.serverAnswerEmail)
                                startActivity(intent)
                            }

                            "false" -> {
                                Toast.makeText(
                                    applicationContext, "Логин или пароль введены неверно!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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

    //Отправка id пользователя в ProfileFragment
    fun getID(id: Int?): ProfileFragment{
        val idUser = Bundle()
        idUser.putInt("id", id!!)
        ProfileFragment().arguments = idUser
        return ProfileFragment()
    }
}
