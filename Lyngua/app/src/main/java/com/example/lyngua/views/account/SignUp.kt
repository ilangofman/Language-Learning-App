package com.example.lyngua.views.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.lyngua.R
import com.example.lyngua.models.User.User
import com.example.lyngua.views.Categories.PracticeDirections
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUp : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        button_sign_up.setOnClickListener {
            val email = email_field.text.toString()
            val password = password_field.text.toString()
            val user = User()
            user.email = email


            val actionChosen = SignUpDirections.actionSignUpToSetupProfile(user)

            navController.navigate(actionChosen)

        }
    }
}