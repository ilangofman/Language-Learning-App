package com.example.lyngua.views.account

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
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import kotlinx.android.synthetic.main.fragment_setup_profile.*

class SetupProfile : Fragment() {

    lateinit var navController: NavController
    private val userPassedIn by navArgs<SetupProfileArgs>()
    private var profileImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        button_next.setOnClickListener {
            userPassedIn.user.firstName = editText_first_name.text.toString()
            userPassedIn.user.lastName = editText_last_name.text.toString()
            if (profileImageUri != null) {
                userPassedIn.user.profilePicture = profileImageUri.toString()
            } else {
                userPassedIn.user.profilePicture = null
            }

            val actionChosen = SetupProfileDirections.actionSetupProfileToChooseLanguage(userPassedIn.user)
            navController.navigate(actionChosen)
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
        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}