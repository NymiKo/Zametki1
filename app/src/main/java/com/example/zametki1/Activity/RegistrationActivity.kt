package com.example.zametki1.Activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.zametki1.R
import com.example.zametki1.RestAPILogin.NetworkService
import com.example.zametki1.RestAPILogin.PostReg
import com.example.zametki1.RestAPILogin.RegistrationRequestModel
import com.example.zametki1.databinding.ActivityRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)

        title = "Регистрация"
        binding.buttonRegistration.setOnClickListener{
            sendGetReg()
        }
    }

    private fun sendGetReg() {
        if(binding.editLoginReg.length() >= 3) {
            //binding.wrongLogin.visibility = View.GONE
            Log.e("OKHTTP3", "функция POST вызвана")
            thread {
                NetworkService.instance()
                    .getJSONApi()
                    .postDataReg(
                        RegistrationRequestModel(
                            binding.editLoginReg.text.toString(),
                            binding.editPasswordReg.text.toString(),
                            binding.editNameReg.text.toString(),
                            binding.editSurnameReg.text.toString(),
                            binding.editNumberPhone.text.toString(),
                            binding.editEmail.text.toString()
                        ).getBody())
                    .enqueue(object : Callback<PostReg> {
                        @Override
                        override fun onResponse(call: Call<PostReg>, response: Response<PostReg>) {
                            Log.e("OKHTTP3", "Все нормально")
                            val post: PostReg? = response.body()
                            Log.e("OKHTTP3", post?.serverAnswerReg.toString())
                            when (post?.serverAnswerReg.toString()) {
                                "true" -> {
                                    startActivity(
                                        Intent(this@RegistrationActivity, MainActivity::class.java)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                                }
                                "false" -> {
                                    Toast.makeText(
                                        applicationContext, "Такой логин уже существует!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        @Override
                        override fun onFailure(call: Call<PostReg>, t: Throwable) {
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
    }
}
