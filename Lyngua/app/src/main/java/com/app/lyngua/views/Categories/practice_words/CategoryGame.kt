package com.app.lyngua.views.Categories.practice_words

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.app.lyngua.R
import com.app.lyngua.models.Question
import com.app.lyngua.controllers.Session
import com.app.lyngua.controllers.UserController
import com.app.lyngua.models.FillInTheBlank
import com.app.lyngua.models.MultipleChoice
import com.app.lyngua.models.User.User
import com.app.lyngua.models.WordMatching
import com.app.lyngua.models.words.GameSessionData

import kotlin.collections.ArrayList

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


        // Get the user to pass to Session class to extrapolate language
        val user: User? = userController.readUserInfo(requireContext())

        // Retrieving the category selected from the practice fragment
        gameSession = Session(args.categoryChosen, user)

        thread {
            questionsList = gameSession.generateSession()
        }.join()

        Log.d("log", "questionList size: ${questionsList.size}")

        // Using a data class to store game information and to pass to subsequent game fragments.
        var sessionTime: Long = System.currentTimeMillis()
        val gameSessionData = GameSessionData(questionsList, args.categoryChosen, 0, 0, wrongAnsMap, currentQuestionPos, sessionTime)
        Log.d("LIST", "${questionsList}")
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