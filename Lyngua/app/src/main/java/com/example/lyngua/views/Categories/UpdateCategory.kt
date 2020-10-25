package com.example.lyngua.views.Categories

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.views.Categories.UpdateCategoryArgs
import com.example.lyngua.controllers.CategoryController
import kotlinx.android.synthetic.main.fragment_update_category.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*


class UpdateCategory : Fragment() {

    private lateinit var categoryController: CategoryController
    private val args by navArgs<UpdateCategoryArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update_category, container, false)

        categoryController = CategoryController(requireContext())

        view.category_name_input_text.setText( args.categoryChosen.name)

        view.update_category_btn.setOnClickListener{
            val categoryName = category_name_input_text.text.toString()
            if(categoryName.isNotEmpty()) {
                val result = categoryController.updateCategory(args.categoryChosen.id, categoryName, args.categoryChosen.numWords+1, args.categoryChosen.wordsList, args.categoryChosen.sessionNumber)
                if(result){
                    Toast.makeText(requireContext(), "Category Has Been Updated", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
                }
            }else{
                Toast.makeText(requireContext(), "Category Name Cannot be Empty", Toast.LENGTH_LONG).show()
            }

        }


        view.delete_category_btn.setOnClickListener{
            val confirmation = AlertDialog.Builder(requireContext())
            confirmation.setTitle("Delete?")
            confirmation.setMessage("Are you sure you would like to delete this category?")
            confirmation.setPositiveButton("Delete"){_,_ ->
                categoryController.deleteCategory(args.categoryChosen)
                Toast.makeText(requireContext(), "Delete Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
            }
            confirmation.setNegativeButton("Cancel"){_,_ ->}
            confirmation.create().show()
        }

        return view
    }


}