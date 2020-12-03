package com.example.lyngua.views.Categories.practice_words

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.models.FillInTheBlank
import com.example.lyngua.models.MultipleChoice
import com.example.lyngua.models.Question
import com.example.lyngua.models.WordMatching
import com.example.lyngua.models.words.GameSessionData
import com.example.lyngua.models.words.Results
import com.example.lyngua.views.Categories.UpdateCategory
import kotlinx.android.synthetic.main.fragment_fill_in_blank.*

class FillInBlank : Fragment() {
    private val args by navArgs<FillInBlankArgs>()

    lateinit var navController : NavController
    lateinit var questionsList: ArrayList<Question>

    private var wrongAnsMap = mutableMapOf<String, String>()
    private var currentQuestionPos = 0
    private var numCorrect = 0
    private var numDone = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fill_in_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        questionsList = args.gameData.questionList
        currentQuestionPos = args.gameData.currentQuestionPos
        wrongAnsMap = args.gameData.wrongAnsMap
        numCorrect = args.gameData.numCorrect

        btn_evaluate.setOnClickListener(clickListener)
        displayQuestion()
        card_view2.visibility = View.INVISIBLE
    }

    private val clickListener = View.OnClickListener {
        keyboard_help.visibility = View.GONE
        //Check if text is empty
        if (editText_translation.text.toString().isEmpty()) {
            Toast.makeText(activity, "Please enter your translation above.", Toast.LENGTH_LONG).show()
        } else {
            evaluateAnswer()
        }
    }

    private fun displayQuestion() {
        val question = questionsList[currentQuestionPos] as FillInTheBlank

        question_word.text = question.displayWord
        card_view2.visibility = View.INVISIBLE
    }

    private fun evaluateAnswer() {
        val question = questionsList[currentQuestionPos] as FillInTheBlank

        card_view2.visibility = View.VISIBLE

        if (editText_translation.text.toString().toLowerCase() == question.correctAnswer.toLowerCase()) {
            result_text.text = "Correct!"
            question.word.correctAnswer()
            numCorrect++
        } else {
            result_text.text = "Incorrect, the correct translation is ${question.correctAnswer}."
            wrongAnsMap[question.displayWord] = question.correctAnswer
            question.word.incorrectAnswer()
        }
        numDone++
        currentQuestionPos++

        Handler().postDelayed({
            if (currentQuestionPos == questionsList.size) {
                if(args.gameData.categoryChosen.goal.goalType == UpdateCategory.SWITCH_ON){
                    args.gameData.categoryChosen.goal.numWordsCompleted += args.gameData.numWordsDone + numDone

                    if(args.gameData.categoryChosen.goal.numWordsCompleted >= args.gameData.categoryChosen.goal.totalNumWords){
                        args.gameData.categoryChosen.goal.goalType = UpdateCategory.SWITCH_OFF
                        args.gameData.categoryChosen.goal.numWordsCompleted = 0
                    }
                }

                val categoryController = CategoryController(requireContext())
                val goal = categoryController.updateCategory(
                    args.gameData.categoryChosen.id,
                    args.gameData.categoryChosen.name,
                    args.gameData.categoryChosen.numWords + 1,
                    args.gameData.categoryChosen.wordsList,
                    args.gameData.categoryChosen.sessionNumber + 1,
                    args.gameData.categoryChosen.goal
                )


                val results = Results(wrongAnsMap, numCorrect, args.gameData.numWordsDone + numDone)
                val action = FillInBlankDirections.actionFillInBlankToCategoryPracticeResults(results)
                navController.navigate(action)
            } else {
                val gameSessionData = GameSessionData(
                    questionsList,
                    args.gameData.categoryChosen,
                    args.gameData.numWordsDone + numDone,
                    numCorrect,
                    wrongAnsMap,
                    currentQuestionPos
                )
                Log.d("endround", "${args.gameData.numWordsDone + numDone}")
                Log.d("endround", "$numCorrect")
                Log.d("endround", "$wrongAnsMap")
                Log.d("endround", "$currentQuestionPos")

                when (questionsList[currentQuestionPos]) {
                    is MultipleChoice -> {
                        val action = FillInBlankDirections.actionFillInBlankToMultipleChoice(gameSessionData)
                        navController.navigate(action)
                    }
                    is WordMatching -> {
                        val action = FillInBlankDirections.actionFillInBlankToWordMatching(gameSessionData)
                        navController.navigate(action)
                    }
                    is FillInTheBlank -> {
                        editText_translation.text?.clear()
                        displayQuestion()
                    }
                }
            }
        }, 2500)
    }


}