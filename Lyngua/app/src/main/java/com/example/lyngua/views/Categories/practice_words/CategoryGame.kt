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
import com.example.lyngua.models.Question
import com.example.lyngua.controllers.Session
import com.example.lyngua.controllers.UserController
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.models.FillInTheBlank
import com.example.lyngua.models.MultipleChoice
import com.example.lyngua.models.User.User
import com.example.lyngua.models.WordMatching
import com.example.lyngua.models.words.GameSessionData
import com.example.lyngua.models.words.Results
import com.example.lyngua.models.words.Word

import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_OFF
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON
import com.example.lyngua.views.Categories.practice_words.CategoryGameArgs
import com.example.lyngua.views.Categories.practice_words.CategoryGameDirections

import kotlinx.android.synthetic.main.fragment_category_game.*

import kotlin.concurrent.thread

class CategoryGame : Fragment() {
    private val args by navArgs<CategoryGameArgs>()

    lateinit var navController : NavController
    lateinit var questionsList: ArrayList<Question>
    lateinit var gameSession: Session
    private var wrongAnsMap = mutableMapOf<String, String>()
    private val userController: UserController = UserController()
    private var currentQuestionPos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_game, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // REMOVE THIS
        //navController.navigate(R.id.action_categoryGame_to_wordMatching)

        // Get the user to pass to Session class to extrapolate language
        val user: User? = userController.readUserInfo(requireContext())

        // Retrieving the category selected from the practice fragment
        gameSession = Session(args.categoryChosen, user)

        thread {
            questionsList = gameSession.generateSession()
        }.join()

        Log.d("log", "questionList size: ${questionsList.size}")

        val gameSessionData = GameSessionData(questionsList, args.categoryChosen, 0, 0, wrongAnsMap, currentQuestionPos)

        when (questionsList[currentQuestionPos]) {
            is MultipleChoice -> {
                val action = CategoryGameDirections.actionCategoryGameToMultipleChoice(gameSessionData)
                navController.navigate(action)
            }
            is WordMatching -> {
                val action = CategoryGameDirections.actionCategoryGameToWordMatching(gameSessionData)
                navController.navigate(action)
            }
            is FillInTheBlank -> {
                val action = CategoryGameDirections.actionCategoryGameToFillInBlank(gameSessionData)
                navController.navigate(action)
            }
        }

    }
}