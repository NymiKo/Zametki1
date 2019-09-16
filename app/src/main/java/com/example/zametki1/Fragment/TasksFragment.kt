package com.example.zametki1.Fragment


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
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
import androidx.core.view.DragStartHelper
import androidx.core.view.MotionEventCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.zametki1.Activity.CreateTasks
import com.example.zametki1.Activity.MainActivity
import com.example.zametki1.Activity.TaskViewActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tasks, container, false
        )
        setHasOptionsMenu(true)

        //Получение почты пользователя
        userEmail = arguments?.getString("email")

        //Получение id от TasksActivity
        id = arguments?.getInt("id")
        Log.e("TasksFragment", id.toString())

        return binding.root
    }

    override fun onResume() {
        //Обновление задач при нажатии на кнопку "Обновить"
        binding.linearLayoutShowTask.removeAllViews()
        showTask(id!!)
        super.onResume()
    }

    //Отображение Overflow Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //Выход из аккаунта
            R.id.go_to_mainActivity -> {
                startActivity(Intent(this.activity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                myPreferences = activity?.getSharedPreferences("Preference", Context.MODE_PRIVATE)!!
                editor = myPreferences.edit()
                editor.putString("Login", "null")
                editor.putString("Password", "null")
                editor.apply()
            }
            //Переход к экрану создания задачи
            R.id.go_to_createTasks -> {
                val intent = Intent(this.activity, CreateTasks::class.java)
                intent.putExtra("id", id)
                intent.putExtra("email", userEmail)
                startActivity(intent)
            }
            //Вызов метода обновления задач
            R.id.update_task -> {
                binding.linearLayoutShowTask.removeAllViews()
                showTask(id!!)
            }
        }

        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(view!!))
                || super.onOptionsItemSelected(item)
    }

    //Отправка данных для отображения списка задач
    fun showTask(Id: Int){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postShowTask(
                    ShowTaskModel(Id).getBody())
                .enqueue(object : Callback<List<PostShowTask>> {
                    @Override
                    override fun onResponse(call: Call<List<PostShowTask>>, response: Response<List<PostShowTask>>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: List<PostShowTask>? = response.body()
                        Log.e("OKHTTP3", post?.get(0)?.serverAnswerTaskName.toString())
                        if(post?.get(0)?.serverAnswerTask != "false"){
                            for(i in 0..post!!.lastIndex){
                                val btnTaskView = Button(activity?.applicationContext)
                                btnTaskView.text = post[i].serverAnswerTaskName
                                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                                //btnTaskView.layoutParams =
                                params.setMargins(0, 10, 0, 0)
                                btnTaskView.tag = post[i].serverAnswerTaskId
                                btnTaskView.textSize = 18F
                                btnTaskView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                                btnTaskView.setPadding(20, 0,0,0)
                                btnTaskView.setBackgroundColor(post[i].serverAnswerTaskColor)
                                btnTaskView.setOnClickListener {
                                    val idBtn = btnTaskView.tag as Int
                                    val textBtn = btnTaskView.text.toString()
                                    val intent = Intent(activity, TaskViewActivity::class.java)
                                    intent.putExtra("idBtn", idBtn)
                                    intent.putExtra("textBtn", textBtn)
                                    intent.putExtra("idUser", id)
                                    startActivity(intent)
                                    Log.e("idBtn", "Id кнопки: $idBtn")
                                }
                                binding.linearLayoutShowTask.addView(btnTaskView, params)
                            }
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<List<PostShowTask>>, t: Throwable) {
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

