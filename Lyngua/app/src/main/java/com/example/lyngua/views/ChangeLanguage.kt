package com.example.lyngua.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.models.Languages
import com.google.cloud.translate.Language
import kotlinx.android.synthetic.main.fragment_change_language.*


class ChangeLanguage : Fragment() {

    private var languageModel: Languages = Languages
    private var languageList: List<Language>? = null
    private lateinit var navController: NavController

    private var currentLanguage: Language? = null

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
        languageList = languageModel.getSupportedAllLanguages()

        if (currentLanguage != null) {
            val currentLanguageButton = LanguageButton(requireContext(), currentLanguage!!)
            currentLanguageButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)
            radioGroup_language_list.addView(currentLanguageButton)
            currentLanguageButton.isChecked = true
        }

        if (languageList != null) {
            for (language in languageList!!) {
                val languageButton = LanguageButton(requireContext(), language)
                languageButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)
                languageButton.setOnClickListener {
                    button_save.visibility = View.VISIBLE
                }
                radioGroup_language_list.addView(languageButton)
            }
        }

        button_save.setOnClickListener {
            currentLanguage = view.findViewById<LanguageButton>(radioGroup_language_list.checkedRadioButtonId).language
            navController.popBackStack()
        }
    }
}