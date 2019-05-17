package com.example.zametki1.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.zametki1.R
import com.example.zametki1.RestAPILogin.*
import com.example.zametki1.databinding.ActivityCreateTasksBinding
import kotlinx.android.synthetic.main.expansion_panel_add_participants.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class CreateTasks : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTasksBinding
    var saveTaskColor: Int = -1
    var idAddParticipantsEditText: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_tasks)

        title = "Новая задача"

        //Выбор цвета
        val btnRed = findViewById<Button>(R.id.btn_task_color_red)
        val btnGreen = findViewById<Button>(R.id.btn_task_color_green)
        val btnYellow = findViewById<Button>(R.id.btn_task_color_yellow)
        val btnOrange = findViewById<Button>(R.id.btn_task_color_orange)
        val btnBlue = findViewById<Button>(R.id.btn_task_color_blue)
        val btnPurple = findViewById<Button>(R.id.btn_task_color_purple)
        btnRed.setOnClickListener{choiceOfColor(btnRed)
            saveTaskColor = resources.getColor(R.color.btn_task_color_red)
        }
        btnGreen.setOnClickListener{choiceOfColor(btnGreen)
            saveTaskColor = resources.getColor(R.color.btn_task_color_green)
        }
        btnYellow.setOnClickListener{choiceOfColor(btnYellow)
            saveTaskColor = resources.getColor(R.color.btn_task_color_yellow)
        }
        btnOrange.setOnClickListener{choiceOfColor(btnOrange)
            saveTaskColor = resources.getColor(R.color.btn_task_color_orange)
        }
        btnBlue.setOnClickListener{choiceOfColor(btnBlue)
            saveTaskColor = resources.getColor(R.color.btn_task_color_blue)
        }
        btnPurple.setOnClickListener{choiceOfColor(btnPurple)
            saveTaskColor = resources.getColor(R.color.btn_task_color_purple)
        }

        binding.btnAddTasks.setOnClickListener {
            for(i in 1..idAddParticipantsEditText) {
                val idParticipantsCount: EditText = linearLayoutAddParticipants.findViewWithTag(i)
                Log.e("inPart", idParticipantsCount.text.toString())
                sendCreateTask(
                    binding.editNameTasks.text.toString(),
                    binding.editTextDescription.text.toString(),
                    saveTaskColor,
                    1,
                    idParticipantsCount.text.toString())
            }
        }

        //Создание TextEdit
        val btnAddParticipants = findViewById<Button>(R.id.btnAddParticipants)
        val layoutAddParticipants = findViewById<LinearLayout>(R.id.linearLayoutAddParticipants)
        btnAddParticipants.setOnClickListener {
            val addParticipantsEditText = EditText(applicationContext)
            idAddParticipantsEditText++
            addParticipantsEditText.hint = "Введите Email участника..."
            addParticipantsEditText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            addParticipantsEditText.tag = idAddParticipantsEditText
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addParticipantsEditText.backgroundTintList = ContextCompat.getColorStateList(this, R.color.button_sign_on)
            }
            addParticipantsEditText.inputType = TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            layoutAddParticipants.addView(addParticipantsEditText)
        }
    }

    fun choiceOfColor(btnChoiceRed: Button? = findViewById(R.id.btn_task_color_red)){
        val btnRed = findViewById<Button>(R.id.btn_task_color_red)
        val btnGreen = findViewById<Button>(R.id.btn_task_color_green)
        val btnYellow = findViewById<Button>(R.id.btn_task_color_yellow)
        val btnOrange = findViewById<Button>(R.id.btn_task_color_orange)
        val btnBlue = findViewById<Button>(R.id.btn_task_color_blue)
        val btnPurple = findViewById<Button>(R.id.btn_task_color_purple)

        if(btnChoiceRed!!.text == "") {
            btnRed.text = ""
            btnGreen.text = ""
            btnYellow.text = ""
            btnOrange.text = ""
            btnBlue.text = ""
            btnPurple.text = ""
            btnChoiceRed.text = "✓  "
        }
        else {
            btnChoiceRed.text = ""
            saveTaskColor = -1
            Log.e("COLOR", saveTaskColor.toString())
        }
    }

    fun sendCreateTask(Name: String, Discription: String, Color: Int, Creator: Int, Participant: String){
            thread {
                NetworkService.instance()
                    .getJSONApi()
                    .postCreateTask(
                        CreateTaskModel(Name, Discription, Color, Creator, Participant).getBody())
                    .enqueue(object : Callback<PostCreateTask> {
                        @Override
                        override fun onResponse(call: Call<PostCreateTask>, response: Response<PostCreateTask>) {
                            Log.e("OKHTTP3", "Все нормально")
                            val post: PostCreateTask? = response.body()
                            Log.e("OKHTTP3", post?.serverAnswerCreateTask.toString())
                            when(post?.serverAnswerCreateTask.toString()){
                                "true" -> {
                                    Toast.makeText(
                                        applicationContext, "Задача добавлена!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                "false" -> {
                                    Toast.makeText(
                                        applicationContext, "Ошибка создания задачи!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                "ErrorParticipant" -> {
                                    Toast.makeText(
                                        applicationContext, "Проверьте все ли участники введены правильно!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        @Override
                        override fun onFailure(call: Call<PostCreateTask>, t: Throwable) {
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
