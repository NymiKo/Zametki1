package com.example.zametki1.Fragment


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
    private var idUser: Int? = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Получение id
        val bundle = arguments
        idUser = bundle?.getInt("id")

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

    override fun onStart() {

        super.onStart()
    }

    private fun editProfile(){
        //Скрытие TextView
        binding.profileNameView.visibility = View.INVISIBLE
        binding.profileSurnameView.visibility = View.INVISIBLE
        binding.profileNumberPhoneView.visibility = View.INVISIBLE
        binding.profileEmailView.visibility = View.INVISIBLE

        //Отображение EditText
        binding.profileEditName.visibility = View.VISIBLE
        binding.profileEditSurname.visibility = View.VISIBLE
        binding.profileEditNumberPhone.visibility = View.VISIBLE
        binding.profileEditEmail.visibility = View.VISIBLE

        binding.profileEditName.text = binding.profileNameView.editableText
        binding.profileEditSurname.text = binding.profileSurnameView.editableText
        binding.profileEditNumberPhone.text = binding.profileNumberPhoneView.editableText
        binding.profileEditEmail.text = binding.profileEmailView.editableText

        //Скрытие ButtonEdit
        binding.profileButtonEdit.visibility = View.INVISIBLE

        //Отображение ButtonSave
        binding.profileButtonSave.visibility = View.VISIBLE
    }

    private fun saveProfile(){
        //Скрытие EditText
        binding.profileEditName.visibility = View.INVISIBLE
        binding.profileEditSurname.visibility = View.INVISIBLE
        binding.profileEditNumberPhone.visibility = View.INVISIBLE
        binding.profileEditEmail.visibility = View.INVISIBLE

        //Отображение TextView
        binding.profileNameView.visibility = View.VISIBLE
        binding.profileSurnameView.visibility = View.VISIBLE
        binding.profileNumberPhoneView.visibility = View.VISIBLE
        binding.profileEmailView.visibility = View.VISIBLE

        binding.profileNameView.text = binding.profileEditName.text
        binding.profileSurnameView.text = binding.profileEditSurname.text
        binding.profileNumberPhoneView.text = binding.profileEditNumberPhone.text
        binding.profileEmailView.text = binding.profileEditEmail.text

        //Скрытие ButtonSave
        binding.profileButtonSave.visibility = View.INVISIBLE

        //Отображение ButtonEdit
        binding.profileButtonEdit.visibility = View.VISIBLE
    }
}
