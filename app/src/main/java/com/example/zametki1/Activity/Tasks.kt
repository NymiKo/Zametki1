package com.example.zametki1.Activity

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import android.util.Log
import android.widget.TextView
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.R
import com.example.zametki1.databinding.ActivityTasksBinding

@Suppress("UNREACHABLE_CODE")
class Tasks : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private lateinit var binding: ActivityTasksBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks)

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)

        //Получение id пользователя
        val id = intent.extras?.get("id").toString()
        Log.e("UserID", id)

        //Получение именя пользователя
        val userName = intent.extras?.get("name").toString()
        Log.e("UserName", userName)

        //Получение email пользователя
        val userEmail = intent.extras?.get("email").toString()

        //Отображение имени и email в navigation header
        navHeaderSet(userName, userEmail)

        //Передача id в TasksFragment
        setID_onTasksFragment(id.toInt(), userEmail)

        //Передача id в ProfileFragment
        setID_onProfileFragment(id.toInt())

        //Отображение Navigation Menu
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }

    //Отображение и функциона кнопки "Назад" для всех фрагментов
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val id = intent.extras?.get("id").toString()
        Log.e("UserID", id)

        //Получение email пользователя
        val userEmail = intent.extras?.get("email").toString()
        setID_onTasksFragment(id.toInt(), userEmail)

        dialogExit()
    }

    //Отправка id и email пользователя в TasksFragment
    fun setID_onTasksFragment(id: Int, userEmail: String){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val infoUser = Bundle()
        infoUser.putInt("id", id)
        infoUser.putString("email", userEmail)
        navController.navigate(R.id.tasksFragment, infoUser)
    }

    //Отправка id пользователя в ProfileFragment
    fun setID_onProfileFragment(id: Int){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val setID = navController.graph.findNode(R.id.profileFragment)
        setID?.addArgument("id", NavArgument.Builder()
            .setType(NavType.IntType)
            .setDefaultValue(id)
            .build())
    }

    //Отображение именя и почты в Navigation Header
    fun navHeaderSet(userName: String, userEmail: String){
        val navHeader = binding.navigationView.getHeaderView(0)
        val userNameTextView = navHeader.findViewById<TextView>(R.id.nav_header_user_name)
        val userEmailTextView = navHeader.findViewById<TextView>(R.id.nav_header_user_email)
        userNameTextView.text = userName
        userEmailTextView.text = userEmail
    }

    private fun dialogExit(){
        val dialogDel = AlertDialog.Builder(this)
        dialogDel.setTitle("Выход")
        dialogDel.setMessage("Вы хотите выйти?")
        dialogDel.setPositiveButton("Нет", DialogInterface.OnClickListener() { dialog: DialogInterface, i: Int -> run{
            dialog.cancel()
        }
        })
        dialogDel.setNegativeButton("Да", DialogInterface.OnClickListener(){ dialog: DialogInterface, i: Int -> run{
            finish()
        }
        })
        dialogDel.show()
    }
}
