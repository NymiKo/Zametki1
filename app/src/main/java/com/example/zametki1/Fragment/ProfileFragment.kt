package com.example.zametki1.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zametki1.databinding.FragmentProfileBinding
import com.example.zametki1.R
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.zametki1.Activity.Tasks
import com.example.zametki1.RestAPILogin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : androidx.fragment.app.Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Получение id пользователя
        val id = arguments?.getInt("id")
        Log.e("ProfileFragment", id.toString())

        //Вызов метода для получения данных профиля пользователя
        viewProfile(id!!)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        saveProfileBtnClick()

        //Вызов метода для редактирования профиля
        binding.profileButtonEdit.setOnClickListener {
            editProfileVtnClick()

            //Скрытие ButtonEdit
            binding.profileButtonEdit.visibility = View.INVISIBLE

            //Отображение ButtonSave
            binding.profileButtonSave.visibility = View.VISIBLE
        }

        //Вызов метода для сохранения измененных данных профиля
        binding.profileButtonSave.setOnClickListener {
            editProfile(
                id.toInt(),
                binding.profileEditName.text.toString(),
                binding.profileEditSurname.text.toString(),
                binding.profileEditNumberPhone.text.toString(),
                binding.profileEditEmail.text.toString()
            )

            saveProfileBtnClick()

            //Скрытие ButtonSave
            binding.profileButtonSave.visibility = View.INVISIBLE

            //Отображение ButtonEdit
            binding.profileButtonEdit.visibility = View.VISIBLE
        }
        return binding.root
    }

    private fun editProfileVtnClick(){
        //Скрытие TextView

        binding.apply {

            //Отображение EditText
            activity?.runOnUiThread {
                binding.profileEditName.isEnabled = true
                binding.profileEditSurname.isEnabled = true
                binding.profileEditEmail.isEnabled = true
                binding.profileEditNumberPhone.isEnabled = true
                binding.profileEditName.isCursorVisible = true
                binding.profileEditSurname.isCursorVisible = true
                binding.profileEditEmail.isCursorVisible = true
                binding.profileEditNumberPhone.isCursorVisible = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    profileEditName.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_dark)
                    profileEditSurname.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_dark)
                    profileEditEmail.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_dark)
                    profileEditNumberPhone.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_dark)
                }
            }
        }
    }

    private fun saveProfileBtnClick(){
        //Скрытие EditText
        binding.apply {
            activity?.runOnUiThread {
                binding.profileEditName.isEnabled = false
                binding.profileEditSurname.isEnabled = false
                binding.profileEditEmail.isEnabled = false
                binding.profileEditNumberPhone.isEnabled = false
                binding.profileEditName.isCursorVisible = false
                binding.profileEditSurname.isCursorVisible = false
                binding.profileEditEmail.isCursorVisible = false
                binding.profileEditNumberPhone.isCursorVisible = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    profileEditName.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_light)
                    profileEditSurname.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_light)
                    profileEditEmail.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_light)
                    profileEditNumberPhone.backgroundTintList = ContextCompat.getColorStateList(activity!!.applicationContext, R.color.background_material_light)
                }
            }
        }
    }

    //Метод для получения данных профиля
    private fun viewProfile(id: Int){
        thread {
            NetworkService.instance()
                .getJSONApi()
                .postViewProfile(
                    ProfileViewModel(id).getBody())
                .enqueue(object : Callback<PostViewProfile> {
                    @Override
                    override fun onResponse(call: Call<PostViewProfile>, response: Response<PostViewProfile>) {
                        Log.e("OKHTTP3", "Все нормально")
                        val post: PostViewProfile? = response.body()
                        activity?.runOnUiThread {
                            binding.apply {
                                profileEditName.setText(post?.serverAnswerName)
                                profileEditSurname.setText(post?.serverAnswerSurname)
                                profileEditNumberPhone.setText(post?.serverAnswerNumberPhone)
                                profileEditEmail.setText(post?.serverAnswerEmail)
                            }
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<PostViewProfile>, t: Throwable) {
                        Log.e("ProfileView", "Ошибка!")
                    }
                })
        }
    }

    //Метод для записи измененых профиля данных в базу данных
    private fun editProfile(Id: Int, Name: String, Surname: String, NumberPhone: String, Email: String){
        if(binding.profileEditName.length() == 0 || binding.profileEditSurname.length() == 0 ||
            binding.profileEditNumberPhone.length() == 0 || binding.profileEditEmail.length() == 0){
            Toast.makeText(
                activity?.applicationContext, "Заполнены не все поля!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            thread {
                NetworkService.instance()
                    .getJSONApi()
                    .postEditProfile(
                        ProfileEditModel(Id, Name, Surname, NumberPhone, Email).getBody())
                    .enqueue(object : Callback<PostEditProfile> {
                        @Override
                        override fun onResponse(call: Call<PostEditProfile>, response: Response<PostEditProfile>) {
                            Log.e("OKHTTP3", "Все нормально")
                            val post: PostEditProfile? = response.body()
                            Log.e("OKHTTP3", post?.serverAnswerEditProfile.toString())
                            when(post?.serverAnswerEditProfile.toString()){
                                "true" -> {
                                    saveProfileBtnClick()
                                    navHeaderSet(binding.profileEditName.text.toString(), binding.profileEditEmail.text.toString())
                                    Toast.makeText(
                                        activity?.applicationContext, "Изменения успешно сохранены!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                "false" -> {
                                    Toast.makeText(
                                        activity?.applicationContext, "Изменения не сохранены!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        @Override
                        override fun onFailure(call: Call<PostEditProfile>, t: Throwable) {
                            Log.e("OKHTTP3", "Что-то пошло не так...")
                            t.printStackTrace()
                            Toast.makeText(
                                activity?.applicationContext, "Ошибка!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Looper.loop()
                        }
                    })
            }
        }
    }

    //Обновление имени и почты в Navigation Header
    fun navHeaderSet(userName: String, userEmail: String){
        val navHeader = activity?.findViewById<View>(R.id.navigationView)
        val userNameTextView = navHeader?.findViewById<TextView>(R.id.nav_header_user_name)
        val userEmailTextView = navHeader?.findViewById<TextView>(R.id.nav_header_user_email)
        userNameTextView?.text = userName
        userEmailTextView?.text = userEmail
    }
}
