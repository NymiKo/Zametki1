package com.example.zametki1.Activity

import android.content.Context
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.Fragment.ProfileFragment
import com.example.zametki1.R
import com.example.zametki1.databinding.ActivityTasksBinding

class Tasks : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private lateinit var myPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityTasksBinding>(this, R.layout.activity_tasks)
        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)
        val id = intent.extras?.get("id").toString()
        saveID_onPreferences(id.toInt())
//        val idUser = Bundle()
//        idUser.putInt("id", id.toInt())
//        ProfileFragment().arguments = idUser
//        navController.navigate(R.id.profileFragment, idUser)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    fun saveID_onPreferences(id: Int){
        myPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)
        editor = myPreferences.edit()
        editor.putInt("id", id)
        editor.apply()
    }
}
