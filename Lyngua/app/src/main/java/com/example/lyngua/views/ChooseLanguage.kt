package com.example.lyngua.views

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.models.Languages
import com.google.cloud.translate.Language

class ChooseLanguage : Fragment() {

    private var languageModel: Languages = Languages
    private var languageList: List<Language>? = null
    private lateinit var navController: NavController
    private lateinit var languageLayout: LinearLayout

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
        languageLayout = view.findViewById(R.id.languageList)

        languageList = languageModel.getSupportedAllLanguages()
        if (languageList != null) {
            for (language in languageList!!) {
                val languageButton = Button(activity)
                languageButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)
                languageButton.text = language.name
                languageButton.setPadding(25, 25, 25, 25)
                languageButton.setBackgroundColor(Color.WHITE)
                languageButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black, 0)
                languageButton.textSize = 20F
                languageButton.gravity = Gravity.START or Gravity.CENTER_VERTICAL

                languageButton.setOnClickListener {
                    navController.navigate(R.id.action_chooseLanguage_to_chooseInterests)
                }
                languageLayout.addView(languageButton)
            }
        }
    }
}