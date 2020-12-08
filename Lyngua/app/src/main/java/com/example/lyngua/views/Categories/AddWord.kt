package com.example.lyngua.views.Categories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.models.words.Word
import kotlinx.android.synthetic.main.fragment_add_word.*
import kotlinx.android.synthetic.main.fragment_create_album.*
import kotlinx.android.synthetic.main.fragment_create_album.button_cancel
import kotlinx.android.synthetic.main.fragment_create_album.button_confirm

class AddWord(arg: UpdateCategoryArgs) : DialogFragment() {

    private lateinit var categoryController: CategoryController
    var args = arg

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryController = CategoryController(requireContext())

        //Cancel button
        button_cancel.setOnClickListener {
            dismiss()
        }

        //Confirm button - Check if text is empty
        button_confirm.setOnClickListener{
            val word = word_text.text.toString().toLowerCase()
            if(word.isNotEmpty()){

                val tempList: List<String> = emptyList()
                val newWord = Word(0, tempList, word)
                var updatedList : MutableList<Word> = args.categoryChosen.wordsList.toMutableList()
                updatedList.add(newWord)
                val result = categoryController.updateCategory(
                    args.categoryChosen.id,
                    args.categoryChosen.name,
                    args.categoryChosen.numWords,
                    updatedList,
                    args.categoryChosen.sessionNumber,
                    args.categoryChosen.goal
                )

                if (result){
                    Toast.makeText(requireContext(), "Added Word: $word", Toast.LENGTH_SHORT).show()
                    dismiss()
                    findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
                }


            }
        }

    }
}
