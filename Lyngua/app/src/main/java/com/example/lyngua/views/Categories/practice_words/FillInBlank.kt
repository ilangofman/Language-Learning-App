package com.example.lyngua.views.Categories.practice_words

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.util.*
import kotlin.collections.ArrayList

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

        // Make the result_text's parent cardview invisible until an answer is given.
        card_view2.visibility = View.INVISIBLE
    }

    // Create an OnClickListener for the "Submit Answer" button
    private val clickListener = View.OnClickListener {
        keyboard_help.visibility = View.GONE
        //Check if text is empty
        if (editText_translation.text.toString().isEmpty()) {
            Toast.makeText(activity, "Please enter your translation above.", Toast.LENGTH_LONG).show()
        } else {
            evaluateAnswer()
        }
    }

    /*
        Name:       displayQuestion
        Purpose:    To display the word that requires the user to translate.
        Input:      None
        Output:     None
     */
    private fun displayQuestion() {
        val question = questionsList[currentQuestionPos] as FillInTheBlank

        question_word.text = question.displayWord
        card_view2.visibility = View.INVISIBLE
    }

    /*
        Name:       evaluateAnswer
        Purpose:    To check whether the entered translation is correct/incorrect against the
                    word displayed. It then handles the action to change
                    game mode fragment based on the next question.
        Input:      None
        Output:     None
     */
    private fun evaluateAnswer() {
        val question = questionsList[currentQuestionPos] as FillInTheBlank

        // Make the result_text's cardview visible to indicate to the user whether they got
        // their translation correct or not.
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

        // Hold the frame for 2.5 seconds, handle end game and next round scenarios
        Handler().postDelayed({
            if (currentQuestionPos == questionsList.size) {
                if(args.gameData.categoryChosen.goal.goalType == UpdateCategory.SWITCH_ON){
                    args.gameData.categoryChosen.goal.numWordsCompleted += args.gameData.numWordsDone + numDone

                    if(args.gameData.categoryChosen.goal.numWordsCompleted >= args.gameData.categoryChosen.goal.totalNumWords){
                        args.gameData.categoryChosen.goal.goalType = UpdateCategory.SWITCH_OFF
                        args.gameData.categoryChosen.goal.numWordsCompleted = 0
                    }
                }
                else if(args.gameData.categoryChosen.goal.goalType == UpdateCategory.SWITCH_ON_TIMEGOAL){
                    var currentTime : Long = System.currentTimeMillis()
                    var timePlayed : Double = (currentTime - args.gameData.sessionTime).toDouble()

                    args.gameData.categoryChosen.goal.timeSpent += (timePlayed/1000/60)

                    if(args.gameData.categoryChosen.goal.timeSpent >= args.gameData.categoryChosen.goal.totalTime){
                        args.gameData.categoryChosen.goal.goalType = UpdateCategory.SWITCH_OFF
                        args.gameData.categoryChosen.goal.timeSpent = 0.0
                        args.gameData.categoryChosen.goal.cancelAlarms(requireContext(),args.gameData.categoryChosen)
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


                val results = Results(args.gameData.categoryChosen.id, args.gameData.categoryChosen.name,System.currentTimeMillis()/1000 , wrongAnsMap, numCorrect, args.gameData.numWordsDone + numDone)
                val action = FillInBlankDirections.actionFillInBlankToCategoryPracticeResults(results)
                navController.navigate(action)
            } else {
                val gameSessionData = GameSessionData(
                    questionsList,
                    args.gameData.categoryChosen,
                    args.gameData.numWordsDone + numDone,
                    numCorrect,
                    wrongAnsMap,
                    currentQuestionPos,
                    args.gameData.sessionTime
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