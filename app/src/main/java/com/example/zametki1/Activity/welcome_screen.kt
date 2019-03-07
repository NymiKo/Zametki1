package com.example.zametki1.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.zametki1.R

class welcome_screen : AppCompatActivity() {
    val welcomeTimeOut: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        Handler().postDelayed(Runnable(){
            startActivity(Intent(this@welcome_screen, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }, welcomeTimeOut)
    }
}
