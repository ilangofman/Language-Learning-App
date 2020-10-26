package com.example.lyngua.views.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.Languages
import com.example.lyngua.views.LanguageButton
import com.google.cloud.translate.Language
import kotlinx.android.synthetic.main.fragment_choose_language.*
import java.util.*

class ChooseLanguage : Fragment() {

    private var languageModel: Languages = Languages
    private var languageList: List<Language>? = null
    private lateinit var navController: NavController
    private val userPassedIn by navArgs<SetupProfileArgs>()
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

        languageList = languageModel.getSupportedAllLanguages()
        languageList?.forEach { language ->
            val languageButton = LanguageButton(requireContext(), language)
            languageButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)
            languageButton.setOnClickListener {
                userPassedIn.user.language = languageButton.language
                userPassedIn.user.dateCreated = Date()
                userController.saveInfo(requireContext(), userPassedIn.user)

                navController.navigate(R.id.action_chooseLanguage_to_chooseInterests)
            }
            radioGroup_language_list.addView(languageButton)
        }
    }
}