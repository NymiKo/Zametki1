package com.example.zametki1.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.Activity.CreateTasks
import com.example.zametki1.Activity.MainActivity
import com.example.zametki1.Activity.Tasks
import com.example.zametki1.databinding.FragmentTasksBinding

import com.example.zametki1.R
import com.example.zametki1.RestAPILogin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass.
 *
 */
class TasksFragment : androidx.fragment.app.Fragment() {

    private lateinit var myPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentTasksBinding
    private var id: Int? = null
    private var userEmail: String? = ""
    var idBtnTaskView: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tasks, container, false
        )
        setHasOptionsMenu(true)

        userEmail = arguments?.getString("email")

        //Получение id от TasksActivity
        id = arguments?.getInt("id")
        Log.e("TasksFragment", id.toString())
        showTask(id!!)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.go_to_mainActivity -> {
                startActivity(Intent(this.activity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                myPreferences = activity?.getSharedPreferences("Preference", Context.MODE_PRIVATE)!!
                editor = myPreferences.edit()
                editor.putString("Login", "null")
                editor.putString("Password", "null")
                editor.apply()
            }
            R.id.go_to_createTasks -> {
                val intent = Intent(this.activity, CreateTasks::class.java)
                intent.putExtra("id", id)
                intent.putExtra("email", userEmail)
                startActivity(intent)
            }
        }

        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(view!!))
                || super.onOptionsItemSelected(item)
    }

    fun showTask(Id: Int){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postShowTask(
                    ShowTaskModel(Id).getBody())
                .enqueue(object : Callback<PostShowTask> {
                    @Override
                    override fun onResponse(call: Call<PostShowTask>, response: Response<PostShowTask>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: PostShowTask? = response.body()
                        Log.e("OKHTTP3", post?.serverAnswerTask.toString())
                        val btnTaskView = Button(activity?.applicationContext)
                        idBtnTaskView++
                        btnTaskView.text = post?.serverAnswerTaskName
                        btnTaskView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        btnTaskView.tag = 6000 + idBtnTaskView
                        btnTaskView.setBackgroundColor(post?.serverAnswerTaskColor!!)
                        binding.linearLayoutShowTask.addView(btnTaskView)
//                        if (post?.serverAnswerTask.toString() == "true"){
//
//                        }
                    }

                    @Override
                    override fun onFailure(call: Call<PostShowTask>, t: Throwable) {
                        Log.e("OKHTTP3", "Что-то пошло не так...")
                        t.printStackTrace()
                        Toast.makeText(
                            activity?.applicationContext, "Нет подключения к интернету!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Looper.loop()
                    }
                })
        }
    }
}
