package com.example.lyngua.views.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.lyngua.R
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.User.User
import kotlinx.android.synthetic.main.fragment_personal_details.*
import kotlinx.android.synthetic.main.fragment_personal_details.imageView_profile


class PersonalDetails : Fragment() {

    private lateinit var navController: NavController
    private var profileImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val user: User? = UserController().readUserInfo(requireContext())
        if (user != null) {
            if (user.profilePicture != null) {
                profileImageUri = Uri.parse(user.profilePicture)
                imageView_profile.setImageURI(profileImageUri)
            }
            editText_first_name.setText(user.firstName)
            editText_last_name.setText(user.lastName)
            editText_email.setText(user.email)
        }

        imageView_edit.setOnClickListener {
            //check runtime permission
            if (allPermissionsGranted()) {
                //permission already granted
                pickImageFromGallery()
            } else {
                //show popup to request runtime permission
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSION_CODE)
            }
        }

        button_save.setOnClickListener {
            if (user != null) {
                user.firstName = editText_first_name.text.toString()
                user.lastName = editText_last_name.text.toString()
                user.email = editText_email.text.toString()
                user.profilePicture = profileImageUri.toString()
                UserController().saveInfo(requireContext(), user)
            }

            navController.popBackStack()
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            profileImageUri = data?.data
            imageView_profile.setImageURI(profileImageUri)
            button_save.visibility = View.VISIBLE
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}