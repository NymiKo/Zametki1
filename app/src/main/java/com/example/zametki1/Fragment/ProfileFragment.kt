package com.example.zametki1.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

        val id = arguments?.getInt("id")
        Log.e("ProfileFragment", id.toString())

        viewProfile(id!!)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        binding.profileButtonEdit.setOnClickListener {
            editProfileVtnClick()
        }

        binding.profileButtonSave.setOnClickListener {
            editProfile(
                id.toInt(),
                binding.profileEditName.text.toString(),
                binding.profileEditSurname.text.toString(),
                binding.profileEditNumberPhone.text.toString(),
                binding.profileEditEmail.text.toString()
            )
        }
        return binding.root
    }

    private fun editProfileVtnClick(){
        //Скрытие TextView
        binding.apply {
            profileNameView.visibility = View.INVISIBLE
            profileSurnameView.visibility = View.INVISIBLE
            profileNumberPhoneView.visibility = View.INVISIBLE
            profileEmailView.visibility = View.INVISIBLE

            //Отображение EditText
            profileEditName.visibility = View.VISIBLE
            profileEditSurname.visibility = View.VISIBLE
            profileEditNumberPhone.visibility = View.VISIBLE
            profileEditEmail.visibility = View.VISIBLE

            activity?.runOnUiThread {
                profileEditName.text = profileNameView.editableText
                profileEditSurname.text = profileSurnameView.editableText
                profileEditNumberPhone.text = profileNumberPhoneView.editableText
                profileEditEmail.text = profileEmailView.editableText
            }

            //Скрытие ButtonEdit
            profileButtonEdit.visibility = View.INVISIBLE

            //Отображение ButtonSave
            profileButtonSave.visibility = View.VISIBLE
        }
    }

    private fun saveProfileBtnClick(){
        //Скрытие EditText
        binding.apply {
            profileEditName.visibility = View.INVISIBLE
            profileEditSurname.visibility = View.INVISIBLE
            profileEditNumberPhone.visibility = View.INVISIBLE
            profileEditEmail.visibility = View.INVISIBLE

            //Отображение TextView
            profileNameView.visibility = View.VISIBLE
            profileSurnameView.visibility = View.VISIBLE
            profileNumberPhoneView.visibility = View.VISIBLE
            profileEmailView.visibility = View.VISIBLE

            activity?.runOnUiThread {
                profileNameView.text = profileEditName.text
                profileSurnameView.text = profileEditSurname.text
                profileNumberPhoneView.text = profileEditNumberPhone.text
                profileEmailView.text = profileEditEmail.text
            }

            //Скрытие ButtonSave
            profileButtonSave.visibility = View.INVISIBLE

            //Отображение ButtonEdit
            profileButtonEdit.visibility = View.VISIBLE
        }
    }

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
                                profileNameView.text = post?.serverAnswerName
                                profileSurnameView.text = post?.serverAnswerSurname
                                profileNumberPhoneView.text = post?.serverAnswerNumberPhone
                                profileEmailView.text = post?.serverAnswerEmail
                            }
                        }
                    }

                    @Override
                    override fun onFailure(call: Call<PostViewProfile>, t: Throwable) {
                        Log.e("ProfileView", "Пиздец")
                    }
                })
        }
    }

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
                                    navHeaderSet(binding.profileNameView.text.toString(), binding.profileEmailView.text.toString())
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

    fun navHeaderSet(userName: String, userEmail: String){
        val navHeader = activity?.findViewById<View>(R.id.navigationView)
        val userNameTextView = navHeader?.findViewById<TextView>(R.id.nav_header_user_name)
        val userEmailTextView = navHeader?.findViewById<TextView>(R.id.nav_header_user_email)
        userNameTextView?.text = userName
        userEmailTextView?.text = userEmail
    }
}
