package com.example.lyngua.views.Categories.practice_words

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.controllers.Session
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.FillInTheBlank
import com.example.lyngua.models.MultipleChoice
import com.example.lyngua.models.Question
import com.example.lyngua.models.User.User
import com.example.lyngua.models.WordMatching
import com.example.lyngua.models.words.GameSessionData
import com.example.lyngua.models.words.Results
import com.example.lyngua.views.Categories.UpdateCategory
import kotlinx.android.synthetic.main.fragment_category_game.*
import kotlinx.android.synthetic.main.fragment_multiple_choice.*
import kotlin.concurrent.thread

class MultipleChoice : Fragment(), View.OnClickListener {
    // CHANGE THIS
    private val args by navArgs<MultipleChoiceArgs>()

    lateinit var navController : NavController
    lateinit var questionsList: ArrayList<Question>

    private var optionsList: ArrayList<TextView> = arrayListOf()
    private var wrongAnsMap = mutableMapOf<String, String>()

    private var currentQuestionPos = 0
    private var numCorrect = 0
    private var numDone = 0
    private var optionSelected = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_choice, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        // Setting onClickListeners for the textfield options
        option_one.setOnClickListener(this)
        option_two.setOnClickListener(this)
        option_three.setOnClickListener(this)
        option_four.setOnClickListener(this)

        questionsList = args.gameData.questionList
        currentQuestionPos = args.gameData.currentQuestionPos
        wrongAnsMap = args.gameData.wrongAnsMap
        numCorrect = args.gameData.numCorrect

        displayQuestion()

    }

    /*
    Function:   defaultOptionsView
    Purpose:    To draw out the options TextView to the initial state (when it is not selected)
     */
    private fun defaultOptionsView(){
        // val opList = ArrayList<TextView>()
        optionsList.add(0, option_one)
        optionsList.add(1, option_two)
        optionsList.add(2, option_three)
        optionsList.add(3, option_four)

        for (op in optionsList){
            op.background = context?.let { ContextCompat.getDrawable(it, R.drawable.mcq_options_border) }
        }
    }

    /*
    Function:   resultView
    Purpose:    To draw out the drawableView resource file so that it shows the user what option
                was right and IF the selected option was wrong, which option was right, indicating
                through the TextView's background colour
    Inputs -    option: indicates the option TextView that was selected
                drawableView: the drawable resource file to use to indicate correct/incorrect
                    options
     */
    private fun resultView(option : Int, drawableView : Int) {
        when (option) {
            1 -> option_one.background = context?.let { ContextCompat.getDrawable(it, drawableView)}
            2 -> option_two.background = context?.let { ContextCompat.getDrawable(it, drawableView)}
            3 -> option_three.background = context?.let { ContextCompat.getDrawable(it, drawableView)}
            4 -> option_four.background = context?.let { ContextCompat.getDrawable(it, drawableView)}
        }
    }

    /*
    Function:   displayQuestion
    Purpose:    To set the text in the elements in the fragment to show the question word
                the text with the corresponding options
     */
    private fun displayQuestion() {
        val question = questionsList[currentQuestionPos] as MultipleChoice

        // Begin with a clear view
        defaultOptionsView()

        // Set up question text and the 4 options
        question_word.text = question.displayWord
        option_one.text = question.optionsList[0]
        option_two.text = question.optionsList[1]
        option_three.text = question.optionsList[2]
        option_four.text = question.optionsList[3]
    }

    /*
    Function:   evaluateChoice
    Purpose:    To check the answer given the TextView (option) selected
    Inputs -    choice: the TextView that was selected.
     */
    private fun evaluateChoice(choice: TextView) {
        val question = questionsList[currentQuestionPos] as MultipleChoice

        // Check whether the choice was right or not, and draw the appropriate
        // option boxes given the correct or incorrect choice.
        if (question.correctAnswer != optionSelected) {
            question.word.incorrectAnswer()
            wrongAnsMap[question.displayWord] = question.optionsList[question.correctAnswer as Int - 1]
            resultView(optionSelected, R.drawable.incorrect_choice)
        } else {
            question.word.correctAnswer()
            numCorrect++
        }
        resultView(question.correctAnswer as Int, R.drawable.correct_choice)
        numDone++
        currentQuestionPos++

        /*
        After the user selects their choice, the screen with the correct and/or incorrect
        options highlighted will hold for 3 seconds before moving onto the next question.
         */
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
                val action = MultipleChoiceDirections.actionMultipleChoiceToCategoryPracticeResults(results)
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
                when (questionsList[currentQuestionPos]) {
                    is MultipleChoice -> {
                        val action = MultipleChoiceDirections.actionMultipleChoiceSelf(gameSessionData)
                        navController.navigate(action)
                    }
                    is WordMatching -> {
                        val action = MultipleChoiceDirections.actionMultipleChoiceToWordMatching(gameSessionData)
                        navController.navigate(action)
                    }
                    is FillInTheBlank -> {
                        val action = MultipleChoiceDirections.actionMultipleChoiceToFillInBlank(gameSessionData)
                        navController.navigate(action)
                    }
                }
            }
        }, 1500)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.option_one -> {
                optionSelected = 1
                evaluateChoice(option_one)
            }
            R.id.option_two -> {
                optionSelected = 2
                evaluateChoice(option_two)
            }
            R.id.option_three -> {
                optionSelected = 3
                evaluateChoice(option_three)
            }
            R.id.option_four -> {
                optionSelected = 4
                evaluateChoice(option_four)
            }
        }
    }


}
