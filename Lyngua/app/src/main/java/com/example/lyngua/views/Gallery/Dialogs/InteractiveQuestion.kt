package com.example.lyngua.views.Gallery.Dialogs

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.lyngua.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_interactive_question.*
import java.util.*

class InteractiveQuestion(private val objectWord: String, private val wordOptions: List<String>, private val correctOption: Int) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interactive_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        question_title_interactive.text = objectWord.capitalize(Locale.getDefault())

        option_1.text = wordOptions[0].capitalize(Locale.getDefault())
        option_2.text = wordOptions[1].capitalize(Locale.getDefault())
        option_3.text = wordOptions[2].capitalize(Locale.getDefault())
        option_4.text = wordOptions[3].capitalize(Locale.getDefault())

        option_1.setOnClickListener{
            if(correctOption == 1) showCorrectButton(option_1)
            else showWrongButton(option_1)
        }

        option_2.setOnClickListener {
            if(correctOption == 2) showCorrectButton(option_2)
            else showWrongButton(option_2)
        }

        option_3.setOnClickListener {
            if(correctOption == 3) showCorrectButton(option_3)
            else showWrongButton(option_3)
        }

        option_4.setOnClickListener {
            if(correctOption == 4) showCorrectButton(option_4)
            else showWrongButton(option_4)
        }
    }

    private fun showCorrectButton(button: Button){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.background.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), R.color.green), BlendMode.SRC_ATOP)
        } else {
            button.background.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), PorterDuff.Mode.SRC_ATOP)
        }

    }
    private fun showWrongButton(button: Button){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.background.colorFilter = BlendModeColorFilter(ContextCompat.getColor(requireContext(), R.color.red), BlendMode.SRC_ATOP)
        } else {
            button.background.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), PorterDuff.Mode.SRC_ATOP)
        }
    }
}