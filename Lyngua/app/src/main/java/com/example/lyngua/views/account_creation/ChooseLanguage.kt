package com.example.lyngua.views.account_creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyngua.R
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.Languages
import com.google.cloud.translate.Language
import kotlinx.android.synthetic.main.fragment_choose_language.*
import java.util.*
import kotlin.concurrent.thread

class ChooseLanguage : Fragment() {

    private var languageModel: Languages = Languages
    private var languageList: List<Language>? = null
    private lateinit var navController: NavController
    private val userPassedIn by navArgs<ChooseLanguageArgs>()
    private val userController:UserController = UserController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        thread {
            languageList = languageModel.getSupportedAllLanguages()
        }.join()

        val adapter = languageList?.let { list -> LanguageListAdapter(list) { languageCallback(it) } }
        val recyclerView = recyclerView_language_list
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun languageCallback(language: Language) {
        userPassedIn.user.language = language
        userPassedIn.user.dateCreated = Date()
        userController.saveInfo(requireContext(), userPassedIn.user)

        navController.navigate(R.id.action_chooseLanguage_to_chooseInterests)
    }
}