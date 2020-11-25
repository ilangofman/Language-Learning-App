package com.example.lyngua.views.account_creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import kotlinx.android.synthetic.main.fragment_complete_sign_up.*

class CompleteSignUp : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        button_lets_go.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        button_no_thanks.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}
