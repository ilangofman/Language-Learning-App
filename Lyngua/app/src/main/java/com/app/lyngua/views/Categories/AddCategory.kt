package com.app.lyngua.views.Categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.lyngua.R
import com.app.lyngua.controllers.CategoryController
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_add_category.view.*


/*

The database was set up through the tutorial found here:
https://www.youtube.com/watch?v=3USvr1Lz8g8

*/

class addCategory : Fragment() {

    private lateinit var categoryController:CategoryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_category, container, false)


        categoryController = CategoryController(requireContext())

        view.add_category_btn.setOnClickListener{
            val categoryName = category_name_input_text.text.toString()
            if(categoryName.isNotEmpty()){
                    val response = categoryController.addCategory(categoryName)
                    if (response){
                        Toast.makeText(requireContext(), "Successfully Added Category", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_addCategory_to_practice)
                    }else{
                        Toast.makeText(requireContext(), "Failed Add Category", Toast.LENGTH_LONG).show()
                    }
            }else{
                Toast.makeText(requireContext(), "Please fill out the field", Toast.LENGTH_LONG).show()
            }

        }
        return view
    }





}