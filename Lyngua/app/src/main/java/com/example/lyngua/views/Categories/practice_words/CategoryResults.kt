package com.example.lyngua.views.Categories.practice_words

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.views.Categories.practice_words.CategoryResultsArgs
import kotlinx.android.synthetic.main.fragment_category_results.*

class CategoryResults : Fragment() {
    private val args by navArgs<CategoryResultsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setResultText()
        setWordList()
    }

//    private fun logResult(){
//        Log.d()
//    }

    /*
    Function:   setResultText
    Purpose:    To fill out the result_text with the appropriate text given the user's score
     */

    private fun setResultText() {
        var percentage: Double = 0.0

        percentage = (args.currentResults.numCorrect.toDouble() / args.currentResults.numQuestions.toDouble()) * 100


        Log.d("Results", "percentage = $percentage")

        when {
            percentage >= 80 -> {
                result_text.text = "Well done! Your score was ${percentage}%"
            }
            percentage >= 60 -> {
                result_text.text = "Not bad! Your score was ${percentage}%"
            }
            else -> {
                result_text.text = "Needs work! Your score was ${percentage}%"
            }
        }
    }

    /*
    Function:   setWordList
    Purpose:    Provide the user with the list of questions they got wrong
     */

    private fun setWordList() {
        var wrongWords = ""

        for ((k, v) in args.currentResults.wrongAnsMap) {
            wrongWords += "$k - $v \n"
        }

        if (wrongWords != "")
            list_wrong.text = wrongWords
    }



}