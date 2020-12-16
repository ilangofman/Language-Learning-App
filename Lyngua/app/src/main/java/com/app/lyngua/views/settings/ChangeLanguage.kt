package com.app.lyngua.views.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lyngua.R
import com.app.lyngua.controllers.UserController
import com.app.lyngua.models.Languages
import com.app.lyngua.models.User.User
import com.app.lyngua.views.account_creation.LanguageListAdapter
import com.google.cloud.translate.Language
import kotlinx.android.synthetic.main.fragment_change_language.*
import kotlin.concurrent.thread


class ChangeLanguage : Fragment() {

    private var languageModel: Languages = Languages
    private var languageList: MutableList<Language>? = null
    private lateinit var navController: NavController

    private var currentLanguage: Language? = null
    private var previousSelection = -1
    private var currentSelection = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        thread {
            languageList = languageModel.getSupportedAllLanguages()?.toMutableList()
        }.join()

        val user: User? = UserController().readUserInfo(requireContext())
        if (user != null) {
            currentLanguage = user.language
        }

        currentLanguage?.let { language ->
            languageList?.remove(language)
            languageList?.add(0, language)
            currentSelection = 0
        }

        val adapter = languageList?.let { list -> LanguageListAdapter(list, currentSelection) { languageCallback(it) } }
        val recyclerView = recyclerView_language_list
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        button_save.setOnClickListener {
            if (user != null) {
                user.language = currentLanguage!!
                UserController().saveInfo(requireContext(), user)
            }

            navController.popBackStack()
        }
    }

    private fun languageCallback(language: Language) {
        button_save.visibility = View.VISIBLE
        currentLanguage = language
    }
}