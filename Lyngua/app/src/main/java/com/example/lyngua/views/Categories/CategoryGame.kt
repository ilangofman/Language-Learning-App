package com.example.lyngua.views.Categories

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.controllers.Question
import com.example.lyngua.controllers.Session
import com.example.lyngua.controllers.UserController
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.models.User.User
import com.example.lyngua.models.words.Results
import kotlinx.android.synthetic.main.fragment_category_game.*

import org.w3c.dom.Text

class CategoryGame : Fragment(), View.OnClickListener {
    private val args by navArgs<CategoryGameArgs>()

    lateinit var navController : NavController
    lateinit var questionsList: ArrayList<Question>
    lateinit var gameSession: Session
    val userController: UserController = UserController()

    var optionsList: ArrayList<TextView> = arrayListOf()
    var wrongAnsMap = mutableMapOf<String, String>()

    var questionIndex : Int = 0
    var correctAnswers : Int = 0
    var optionSelected : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // Get the user to pass to Session class to extrapolate language
        val user: User? = userController.readUserInfo(requireContext())

        // Retrieving the category selected from the practice fragment
        gameSession = Session(args.categoryChosen, user)

        questionsList = gameSession.generateSession()

        // Setting onClickListeners for the textfield options
        option_one.setOnClickListener(this)
        option_two.setOnClickListener(this)
        option_three.setOnClickListener(this)
        option_four.setOnClickListener(this)


        displayQuestion()

        /*
        Log.d("Question", "${questionsList[2].displayWord}")
        Log.d("Question", "${questionsList[2].correctAnswer}")
        Log.d("Question", "${questionsList[2].optionsList}")
        */

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
        val question = questionsList[questionIndex]

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
        val question = questionsList[questionIndex]

        // Check whether the choice was right or not, and draw the appropriate
        // option boxes given the correct or incorrect choice.
        if (question.correctAnswer != optionSelected) {
            question.word.incorrectAnswer()
            wrongAnsMap[question.displayWord] = question.optionsList[question.correctAnswer - 1]
            resultView(optionSelected, R.drawable.incorrect_choice)
        } else {
            question.word.correctAnswer()
            correctAnswers++
        }
        resultView(question.correctAnswer, R.drawable.correct_choice)
        questionIndex++

        /*
        After the user selects their choice, the screen with the correct and/or incorrect
        options highlighted will hold for 3 seconds before moving onto the next question.
         */
        Handler().postDelayed({
            if (questionIndex == questionsList.size) {

                if(args.categoryChosen.goal.goalType == 0){
                    args.categoryChosen.goal.numWordsCompleted += gameSession.WORDS_PER_SESSION

                    if(args.categoryChosen.goal.numWordsCompleted >= args.categoryChosen.goal.totalNumWords){
                        args.categoryChosen.goal.goalType = -1
                        args.categoryChosen.goal.numWordsCompleted = 0
                    }
                }

                val categoryController = CategoryController(requireContext())
                val goal = categoryController.updateCategory(
                    args.categoryChosen.id,
                    args.categoryChosen.name,
                    args.categoryChosen.numWords + 1,
                    args.categoryChosen.wordsList,
                    args.categoryChosen.sessionNumber + 1,
                    args.categoryChosen.goal,
                )

                Log.d("Answers", "Number of correct answers: $correctAnswers")
                val results = Results(wrongAnsMap, correctAnswers, questionsList.size)
                val action = CategoryGameDirections.actionCategoryGameToCategoryResults(results)
                navController.navigate(action)

            } else {
                displayQuestion()
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