package com.example.lyngua.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lyngua.R
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.User.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*

class Home : Fragment() {

    lateinit var navController: NavController
    lateinit var navBar: BottomNavigationView
    val userController: UserController = UserController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.visibility = View.VISIBLE

        val user: User? = userController.readUserInfo(requireContext())
        if(user == null){
            navBar.visibility = View.GONE
            navController.navigate(R.id.action_homePage_to_start_navigation)
        }else{
            text_view_home_page.text = user.firstName
        }


        view.findViewById<Button>(R.id.button).setOnClickListener {
            navBar.visibility = View.GONE
            navController.navigate(R.id.action_homePage_to_start_navigation)
        }
    }
}