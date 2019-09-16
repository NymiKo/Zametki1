package com.example.zametki1.Activity

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.zametki1.R
import com.example.zametki1.RestAPILogin.*
import com.example.zametki1.databinding.ActivityTaskViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class TaskViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskViewBinding
    var idBtn: Int? = null
    var textBtn: String = ""
    var idUser: String = ""
    var oldTaskDescription: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_view)

        //Получение id задачи
        idBtn = intent.extras?.get("idBtn") as Int?

        //Получение заголовка задачи
        textBtn = intent.extras?.get("textBtn").toString()

        //Получение id пользователя
        idUser = intent.extras?.get("idUser").toString()

        //Заголовок экрана
        title = textBtn

        //Отображение кнопки "Назад"
        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Вызов метода для отображения данных задачи
        TaskView(idBtn!!.toInt())

        textEdit_in_textView()

        //Вызов диалога подтверждения удаления задачи
        binding.btnDeleteTask.setOnClickListener {
            dialogDelete()
        }

        binding.btnEditTask.setOnClickListener {
            textView_in_textEdit()
            binding.btnDeleteTask.visibility = View.GONE
            binding.btnEditTask.visibility = View.GONE
            binding.btnSaveTask.visibility = View.VISIBLE
            binding.textViewTaskName.visibility = View.VISIBLE
            binding.textName.visibility = View.VISIBLE
            binding.textViewTaskName.setText(textBtn)
            oldTaskDescription = binding.textViewTaskDescription.text.toString()
        }

        binding.btnSaveTask.setOnClickListener {
            val taskName = binding.textViewTaskName.text.toString()
            val taskDescription = binding.textViewTaskDescription.text.toString()
            TaskEdit(idUser.toInt(), textBtn, oldTaskDescription, taskName, taskDescription)
            textEdit_in_textView()
            binding.btnDeleteTask.visibility = View.VISIBLE
            binding.btnEditTask.visibility = View.VISIBLE
            binding.btnSaveTask.visibility = View.GONE
            binding.textViewTaskName.visibility = View.GONE
            binding.textName.visibility = View.GONE
            title = binding.textViewTaskName.text
        }
    }

    //Возвращение на предыдущий экарн при нажатии на кнопку "Назад"
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun textEdit_in_textView(){
        runOnUiThread {
            binding.textViewTaskDescription.isEnabled = false
            binding.textViewTaskDescription.isCursorVisible = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.textViewTaskDescription.backgroundTintList = ContextCompat.getColorStateList(this, R.color.background_material_light)
            }
        }
    }

    private fun textView_in_textEdit(){
        runOnUiThread {
            binding.textViewTaskDescription.isEnabled = true
            binding.textViewTaskDescription.isCursorVisible = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.textViewTaskDescription.backgroundTintList = ContextCompat.getColorStateList(this, R.color.background_material_dark)
            }
        }
    }

    //Отправка запроса на сервер, для получения и отображения данных задачи
    private fun TaskView(idTask: Int){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postTaskView(
                    TaskViewModel(idTask).getBody())
                .enqueue(object : Callback<PostTaskView> {
                    @Override
                    override fun onResponse(call: Call<PostTaskView>, response: Response<PostTaskView>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: PostTaskView? = response.body()
                        if(post?.serverAnswerTask == "true"){
                            binding.textViewTaskDescription.setText(post.serverAnswerTaskDescription)
                            if(idUser.toInt() == post.serverAnswerTaskIdCreator){
                                binding.btnEditTask.visibility = View.VISIBLE
                                binding.btnDeleteTask.visibility = View.VISIBLE
                            }
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<PostTaskView>, t: Throwable) {
                        Log.e("ProfileView", "Ошибка!")
                    }
                })
        }
    }

    //Отправка запроса на сервер, для удаления задачи из базы данных
    private fun TaskDelete(idTask: Int, taskName: String, taskDescription: String){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postTaskDelete(
                    TaskDeleteModel(idTask, taskName, taskDescription).getBody())
                .enqueue(object : Callback<PostTaskDelete> {
                    @Override
                    override fun onResponse(call: Call<PostTaskDelete>, response: Response<PostTaskDelete>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: PostTaskDelete? = response.body()
                        if(post?.serverAnswerTask == "true"){
                            Toast.makeText(
                                applicationContext, "Задача удалена!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<PostTaskDelete>, t: Throwable) {
                        Log.e("ProfileView", "Ошибка!")
                    }
                })
        }
    }

    //Отображение диалога для подтверждения удаления задачи
    private fun dialogDelete(){
        val dialogDel = AlertDialog.Builder(this)
        dialogDel.setTitle("Удаление задачи")
        dialogDel.setMessage("Удалить задачу?")
        dialogDel.setPositiveButton("Нет", DialogInterface.OnClickListener() { dialog: DialogInterface, i: Int -> run{
                dialog.cancel()
            }
        })
        dialogDel.setNegativeButton("Да", DialogInterface.OnClickListener(){ dialog: DialogInterface, i: Int -> run{
                TaskDelete(idUser.toInt(), textBtn, binding.textViewTaskDescription.text.toString())
                this.finish()
            }
        })
        dialogDel.show()
    }

    private fun TaskEdit(CreatorId: Int, OldTaskName: String, OldTaskDescription: String, TaskName: String, TaskDescription: String){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postTaskEdit(
                    TaskEditModel(CreatorId, OldTaskName, OldTaskDescription, TaskName, TaskDescription).getBody())
                .enqueue(object : Callback<PostTaskEdit> {
                    @Override
                    override fun onResponse(call: Call<PostTaskEdit>, response: Response<PostTaskEdit>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: PostTaskEdit? = response.body()
                        if(post?.serverAnswerTask == "true"){
                            Toast.makeText(
                                applicationContext, "Задача изменена!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<PostTaskEdit>, t: Throwable) {
                        Log.e("ProfileView", "Ошибка!")
                    }
                })
        }
    }
}
