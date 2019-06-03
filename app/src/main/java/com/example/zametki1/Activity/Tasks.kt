package com.example.zametki1.Activity

import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import android.util.Log
import android.widget.TextView
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.R
import com.example.zametki1.databinding.ActivityTasksBinding

class Tasks : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private lateinit var binding: ActivityTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks)

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)
        val id = intent.extras?.get("id").toString()
        Log.e("UserID", id)
        val userName = intent.extras?.get("name").toString()
        Log.e("UserName", userName)
        val userEmail = intent.extras?.get("email").toString()

        //Отображение имени и email в navigation header
        navHeaderSet(userName, userEmail)

        //Передача id в TasksFragment
        setID_onTasksFragment(id.toInt(), userEmail)

        //Передача id в ProfileFragment
        setID_onProfileFragment(id.toInt())

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    fun setID_onTasksFragment(id: Int, userEmail: String){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val infoUser = Bundle()
        infoUser.putInt("id", id)
        infoUser.putString("email", userEmail)
        navController.navigate(R.id.tasksFragment, infoUser)
    }

    fun setID_onProfileFragment(id: Int){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val setID = navController.graph.findNode(R.id.profileFragment)
        setID?.addArgument("id", NavArgument.Builder()
            .setType(NavType.IntType)
            .setDefaultValue(id)
            .build())
    }

    fun navHeaderSet(userName: String, userEmail: String){
        val navHeader = binding.navigationView.getHeaderView(0)
        val userNameTextView = navHeader.findViewById<TextView>(R.id.nav_header_user_name)
        val userEmailTextView = navHeader.findViewById<TextView>(R.id.nav_header_user_email)
        userNameTextView.text = userName
        userEmailTextView.text = userEmail
    }
}
