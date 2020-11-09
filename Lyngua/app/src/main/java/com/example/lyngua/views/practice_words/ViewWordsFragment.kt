package com.example.lyngua.views.practice_words

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.views.Categories.UpdateCategoryArgs
import kotlinx.android.synthetic.main.fragment_update_category.view.*
import kotlinx.android.synthetic.main.fragment_view_words.view.*

class ViewWordsFragment : Fragment() {
    private lateinit var categoryController: CategoryController
    private val args by navArgs<UpdateCategoryArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_words, container, false)

        categoryController = CategoryController(requireContext())

        var wordList: String = ""
        for(word in args.categoryChosen.wordsList){
            wordList += word.word + "\n"
        }

        view.word_text_view.text = wordList


        return view
    }


}