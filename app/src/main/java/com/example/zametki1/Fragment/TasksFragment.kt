package com.example.zametki1.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.Activity.MainActivity
import com.example.zametki1.Activity.Tasks
import com.example.zametki1.databinding.FragmentTasksBinding

import com.example.zametki1.R

/**
 * A simple [Fragment] subclass.
 *
 */
class TasksFragment : androidx.fragment.app.Fragment() {

    private lateinit var myPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tasks, container, false
        )
        setHasOptionsMenu(true)

        //Получение id от TasksActivity
        val id = arguments?.getInt("id")
        Log.e("TasksFragment", id.toString())

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
                editor = myPreferences.edit()
                editor.putString("Login", "null")
                editor.putString("Password", "null")
                editor.apply()
            }
        }

        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(view!!))
                || super.onOptionsItemSelected(item)
    }
}
