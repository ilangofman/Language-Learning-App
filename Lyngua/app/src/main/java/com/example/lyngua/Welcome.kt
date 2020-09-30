package com.example.lyngua

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_welcome.*


class Welcome : Fragment(){

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.log_in_btn_welcome_page).setOnClickListener{
           navController.navigate(R.id.action_welcome_to_logInFragment)
        }


        home_page_btn_welcome_page.setOnClickListener{
            Log.d("Welcome Screen", "Create Account Button Pressed")
            val intent = Intent(getActivity(), ActivityTabs::class.java)
            startActivity(intent)
        }

        create_account_btn_welcome_page.setOnClickListener{
            navController.navigate(R.id.action_welcome_to_createAccountFragment)
        }


    }


}