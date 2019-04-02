package com.example.zametki1.Fragment


import android.content.Context
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zametki1.databinding.FragmentProfileBinding
import com.example.zametki1.R
import android.util.Log


/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var id: Int? = 0
    private lateinit var myPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Получение id
        myPreferences = this.activity!!.getSharedPreferences("Preference", Context.MODE_PRIVATE)
        id = myPreferences.getInt("id", 0)
        //id = arguments?.getInt("id")
        Log.e("ProfileFragment", id.toString())

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        binding.profileButtonEdit.setOnClickListener {
            editProfile()
        }

        binding.profileButtonSave.setOnClickListener {
            saveProfile()
        }
        return binding.root
    }

    private fun editProfile(){
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

            profileEditName.text = profileNameView.editableText
            profileEditSurname.text = profileSurnameView.editableText
            profileEditNumberPhone.text = profileNumberPhoneView.editableText
            profileEditEmail.text = profileEmailView.editableText

            //Скрытие ButtonEdit
            profileButtonEdit.visibility = View.INVISIBLE

            //Отображение ButtonSave
            profileButtonSave.visibility = View.VISIBLE
        }
    }

    private fun saveProfile(){
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

            profileNameView.text = profileEditName.text
            profileSurnameView.text = profileEditSurname.text
            profileNumberPhoneView.text = profileEditNumberPhone.text
            profileEmailView.text = profileEditEmail.text

            //Скрытие ButtonSave
            profileButtonSave.visibility = View.INVISIBLE

            //Отображение ButtonEdit
            profileButtonEdit.visibility = View.VISIBLE
        }
    }
}
