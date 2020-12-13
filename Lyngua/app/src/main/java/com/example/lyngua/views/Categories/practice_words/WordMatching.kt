package com.example.lyngua.views.Categories.practice_words

import android.content.ClipData
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.fragment_word_matching.*

class WordMatching : Fragment() {
    private val args by navArgs<WordMatchingArgs>()
    lateinit var navController: NavController
    lateinit var questionsList: ArrayList<Question>
    private var currentQuestionPos = 0
    private var numCorrect = 0
    private var wrongAnsMap = mutableMapOf<String, String>()
    private var numWordsToMatch = 0
    private var valsList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word_matching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        option1.setOnTouchListener(touchListener)
        option2.setOnTouchListener(touchListener)
        option3.setOnTouchListener(touchListener)
        option4.setOnTouchListener(touchListener)

        row_12.setOnDragListener(dragListener)
        row_22.setOnDragListener(dragListener)
        row_32.setOnDragListener(dragListener)
        row_42.setOnDragListener(dragListener)

        btn_evaluate.setOnClickListener(clickListener)
        btn_evaluate.isClickable = true

        questionsList = args.gameData.questionList
        currentQuestionPos = args.gameData.currentQuestionPos
        numCorrect = args.gameData.numCorrect
        wrongAnsMap = args.gameData.wrongAnsMap


        displayQuestion()

    }

    // Create an OnClickListener to attach to "Evaluate Choices" button
    private val clickListener = View.OnClickListener {
        Log.d("log", "lltop child count: ${lltop.childCount}")
        Log.d("log", "llbottom child count: ${llbottom.childCount}")
        if (row_12.childCount == 1 && row_22.childCount == 1 &&
            row_32.childCount == 1 && row_42.childCount == 1) {
            evaluateMatches()
        } else {
            Toast.makeText(activity, "Please add ONLY ONE option to each row.", Toast.LENGTH_SHORT).show()
        }

    }

    /*
        Parts of touchListener and dragListener are made using this website:
        https://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
     */

    // Create an OnTouchListener to attach to View's
    private val touchListener = View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(
                v)
            v.startDrag(data, shadowBuilder, v, 0)
            v.visibility = View.INVISIBLE
            true
        } else {
            false
        }
    }

    // Create an OnDragListener to attach to the options to drag and drop
    private val dragListener = View.OnDragListener { v, event ->
        val enterShape = resources.getDrawable(R.drawable.drop_selection_background)
        val normalShape = resources.getDrawable(R.drawable.drop_deselection_background)
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                v.setBackgroundDrawable(enterShape)
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                v.setBackgroundDrawable(normalShape)
                true
            }
            DragEvent.ACTION_DROP -> {
                // Dropped, reassign View to ViewGroup
                val view = event.localState as TextView
                view.gravity = Gravity.CENTER

                Log.d("log", "view = $view")
                Log.d("log", "view.parent = ${view.parent}")
                val owner = view.parent as ViewGroup
                Log.d("log", "owner = $owner")
                owner.removeView(view)
                val container = v as LinearLayout
                Log.d("log", "container = $container")
                container.addView(view)
                view.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                v.setBackgroundDrawable(normalShape)
                if (event.result){
                    true
                }
                else {
                    val view = event.localState as TextView
                    view.visibility = View.VISIBLE
                    true
                }
            }
            else -> {
                false
            }
        }
    }

    /*
        Name:       displayQuestion
        Purpose:    To be able to display the 4 different options that the user must match the
                    translations to.
        Input:      None
        Output:     None
     */
    private fun displayQuestion(){
        val question = questionsList[currentQuestionPos] as WordMatching
        val keyList = ArrayList<String>()

        for ((k,v) in question.optionsMap){
            keyList.add(k)
            valsList.add(v)
        }

        val shuffledOptions = valsList.shuffled()
        numWordsToMatch = keyList.size

        option1.text = shuffledOptions[0]
        option2.text = shuffledOptions[1]
        option3.text = shuffledOptions[2]
        option4.text = shuffledOptions[3]

        word1.text = keyList[0]
        word2.text = keyList[1]
        word3.text = keyList[2]
        word4.text = keyList[3]

    }

    /*
        Name:       evaluateMatches
        Purpose:    To check whether the translations that the user has dragged to the
                    row of the word is correct. It then handles the action to change
                    game mode fragment based on the next question.
        Input:      None
        Output:     None
     */
    private fun evaluateMatches(){
        val question = questionsList[currentQuestionPos] as WordMatching
        val wordList: ArrayList<TextView> = arrayListOf()
        val tvArrayList: ArrayList<TextView> = arrayListOf()
        val rowCheckBools: ArrayList<Boolean> = arrayListOf()

        // Make the evaluation button unclickable
        btn_evaluate.isClickable = false

        val tv1 : TextView = row_12.getChildAt(0) as TextView
        val tv2 : TextView = row_22.getChildAt(0) as TextView
        val tv3 : TextView = row_32.getChildAt(0) as TextView
        val tv4 : TextView = row_42.getChildAt(0) as TextView

        tvArrayList.add(0, tv1)
        tvArrayList.add(1, tv2)
        tvArrayList.add(2, tv3)
        tvArrayList.add(3, tv4)

        wordList.add(0, word1)
        wordList.add(1, word2)
        wordList.add(2, word3)
        wordList.add(3, word4)

        for (tv in tvArrayList) {
            val currentIdx = tvArrayList.indexOf(tv)

            if (tv.text.toString() == valsList[currentIdx]) {
                rowCheckBools.add(currentIdx, element = true)
                numCorrect++

                if (tv.text.toString() == question.displayWord)
                    question.word.correctAnswer()

            } else {
                rowCheckBools.add(currentIdx, element = false)
                wrongAnsMap[wordList[currentIdx].text.toString()] = valsList[currentIdx]

                if (tv.text.toString() == question.displayWord)
                    question.word.incorrectAnswer()
            }
        }

        Log.d("evaluate", "rowCheckBools: $rowCheckBools")
        Log.d("evaluate", "wrongAnsMap: $wrongAnsMap")


        // Changing the border on the word rows to indicate correctness
        setBackground(rowCheckBools)

        currentQuestionPos++

        // Hold the frame for 2.5 seconds, handle end game and next round scenarios
        Handler().postDelayed({
            if (currentQuestionPos == questionsList.size) {
                if(args.gameData.categoryChosen.goal.goalType == UpdateCategory.SWITCH_ON){
                    args.gameData.categoryChosen.goal.numWordsCompleted += args.gameData.numWordsDone + numWordsToMatch

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


                val results = Results( args.gameData.categoryChosen.id,args.gameData.categoryChosen.name,System.currentTimeMillis()/1000 , wrongAnsMap, numCorrect, args.gameData.numWordsDone + numWordsToMatch)
                val action = WordMatchingDirections.actionWordMatchingToCategoryPracticeResults(results)
                navController.navigate(action)
            } else {
                val gameSessionData = GameSessionData(
                    questionsList,
                    args.gameData.categoryChosen,
                    args.gameData.numWordsDone + numWordsToMatch,
                    numCorrect,
                    wrongAnsMap,
                    currentQuestionPos
                )
                Log.d("endround", "${args.gameData.numWordsDone + numWordsToMatch}")
                Log.d("endround", "$numCorrect")
                Log.d("endround", "$wrongAnsMap")
                Log.d("endround", "$currentQuestionPos")

                when (questionsList[currentQuestionPos]) {
                    is MultipleChoice -> {
                        val action = WordMatchingDirections.actionWordMatchingToMultipleChoice(gameSessionData)
                        navController.navigate(action)
                    }
                    is WordMatching -> {
                        val action = WordMatchingDirections.actionWordMatchingSelf(gameSessionData)
                        navController.navigate(action)
                    }
                    is FillInTheBlank -> {
                        val action = WordMatchingDirections.actionWordMatchingToFillInBlank(gameSessionData)
                        navController.navigate(action)
                    }
                }
            }
        }, 2500)


    }

    /*
        Name:       setBackground
        Purpose:    To set the borders of the word rows to indicate whether the user got the
                    translation correct or not
        Input:      boolList - an array of booleans, row-wise
        Output:     None
     */
    private fun setBackground(boolList: ArrayList<Boolean>) {
        if (boolList[0]) {
            row_one.background = context?.let { ContextCompat.getDrawable(it, R.drawable.correct_table_borders)}
            row_12.background = context?.let { ContextCompat.getDrawable(it, R.drawable.correct_three_sides) }
        }
        else {
            row_one.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_table_borders) }
            row_12.background = context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_three_sides) }
        }

        if (boolList[1]) {
            row_two.background = context?.let { ContextCompat.getDrawable(it, R.drawable.correct_table_borders)}
            row_22.background = context?.let { ContextCompat.getDrawable(it, R.drawable.correct_three_sides) }
        }
        else {
            row_two.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_table_borders) }
            row_22.background = context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_three_sides) }
        }

        if (boolList[2]) {
            row_three.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.correct_table_borders) }
            row_32.background = context?.let { ContextCompat.getDrawable(it, R.drawable.correct_three_sides) }
        }
        else {
            row_three.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_table_borders) }
            row_32.background = context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_three_sides) }
        }

        if (boolList[3]) {
            row_four.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.correct_table_borders) }
            row_42.background = context?.let { ContextCompat.getDrawable(it, R.drawable.correct_three_sides) }
        }
        else {
            row_four.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_table_borders) }
            row_42.background = context?.let { ContextCompat.getDrawable(it, R.drawable.incorrect_three_sides) }
        }
    }

}