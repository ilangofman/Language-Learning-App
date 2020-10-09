package com.example.lyngua.views.addCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_add_category.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class addCategory : Fragment() {

    private lateinit var userController:CategoryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_category, container, false)


        userController = CategoryController(requireContext())

        view.add_category_btn.setOnClickListener{
            val categoryName = category_name_input_text.text.toString()
            if(inputValidation(categoryName)){
                    val response = userController.addCategory(categoryName)
                    if (response){
                        Toast.makeText(requireContext(), "Successfully Added Category", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_addCategory_to_practiceMode)
                    }else{
                        Toast.makeText(requireContext(), "Failed Add Category", Toast.LENGTH_LONG).show()
                    }
            }else{
                Toast.makeText(requireContext(), "Please fill out the field", Toast.LENGTH_LONG).show()
            }

        }
        return view
    }


    private fun inputValidation(categoryName: String): Boolean {
        return categoryName.isNotEmpty()
    }



}