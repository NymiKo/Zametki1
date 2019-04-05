package com.example.zametki1.Activity

import android.content.Context
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.util.Log
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.Fragment.ProfileFragment
import com.example.zametki1.Fragment.TasksFragment
import com.example.zametki1.R
import com.example.zametki1.databinding.ActivityTasksBinding

class Tasks : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityTasksBinding>(this, R.layout.activity_tasks)
        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)
        val id = intent.extras?.get("id").toString()

        //Передача id в TasksFragment
        setID_onTasksFragment(id.toInt())

        //Передача id в ProfileFragment
        setID_onProfileFragment(id.toInt())

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    fun setID_onTasksFragment(id: Int){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val idUser = Bundle()
        idUser.putInt("id", id.toInt())
        navController.navigate(R.id.tasksFragment, idUser)
    }

    fun setID_onProfileFragment(id: Int){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val setID = navController.graph.findNode(R.id.profileFragment)
        setID?.addArgument("id", NavArgument.Builder()
            .setType(NavType.IntType)
            .setDefaultValue(id)
            .build())
    }
}
