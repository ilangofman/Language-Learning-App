package com.example.lyngua.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isNotEmpty
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import kotlinx.android.synthetic.main.fragment_choose_interests.*

class ChooseInterests : Fragment() {

    lateinit var navController: NavController
    lateinit var categoryController: CategoryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_interests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        categoryController = CategoryController(requireContext())

        button_next.setOnClickListener {
            navController.navigate(R.id.action_chooseInterests_to_completeSignUp)
            if(editText_interest1.text.toString().isNotEmpty()){
                categoryController.addCategory(editText_interest1.text.toString())
            }
            if(editText_interest2.text.toString().isNotEmpty()){
                categoryController.addCategory(editText_interest2.text.toString())
            }
            if(editText_interest3.text.toString().isNotEmpty()){
                categoryController.addCategory(editText_interest3.text.toString())
            }
        }
    }
}