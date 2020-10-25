package com.example.lyngua.views.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.models.User.User
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
            val email = editText_email.text.toString()
            val password = editText_password.text.toString()
            val user = User()
            user.email = email


            val actionChosen = SignUpDirections.actionSignUpToSetupProfile(user)

            navController.navigate(actionChosen)

        }
    }
}