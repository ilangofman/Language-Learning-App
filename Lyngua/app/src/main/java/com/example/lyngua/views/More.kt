package com.example.lyngua.views

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.User.User
import kotlinx.android.synthetic.main.fragment_more.*
import java.text.SimpleDateFormat
import java.util.*

class More : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val user: User? = UserController().readUserInfo(requireContext())
        if (user != null) {
            textView_name.text = user.firstName + " " + user.lastName
            textView_language.text = user.language.name
            if (user.profilePicture != null) {
                imageView_profile.setImageURI(Uri.parse(user.profilePicture))
            }

            val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
            textView_date_created.text = "Learning since " + currentDate.format(user.dateCreated)
        }


        button_personal.setOnClickListener {
            navController.navigate(R.id.action_more_to_personalDetails)
        }

        button_language.setOnClickListener {
            navController.navigate(R.id.action_more_to_changeLanguage)
        }
    }
}