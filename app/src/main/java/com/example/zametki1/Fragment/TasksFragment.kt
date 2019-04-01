package com.example.zametki1.Fragment


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.zametki1.databinding.FragmentTasksBinding

import com.example.zametki1.R

/**
 * A simple [Fragment] subclass.
 *
 */
class TasksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTasksBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tasks, container, false
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, Navigation.findNavController(view!!))
                || super.onOptionsItemSelected(item)
    }
}
