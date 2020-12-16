package com.app.lyngua.views.Gallery.Dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.lyngua.R
import com.app.lyngua.controllers.GalleryController
import kotlinx.android.synthetic.main.fragment_edit_album_name.*
import java.util.*

class EditAlbumName(private var albumName: String) : DialogFragment() {

    private lateinit var galleryController: GalleryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_album_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryController = GalleryController(requireContext(), requireActivity(), resources.getString(
            R.string.app_name))

        editText_album_name.setText(albumName)
        editText_album_name.hint = albumName

        //Cancel button - Dismiss dialog
        button_cancel.setOnClickListener {
            dismiss()
        }

        //Confirm button - Check if text is empty and send album name back to parent fragment
        button_confirm.setOnClickListener {
            val albumName = editText_album_name.text.toString().toUpperCase(Locale.getDefault())
            if(albumName.isNotEmpty()){
                val bundle = Bundle()
                bundle.putString("newAlbumName", albumName)

                val intent = Intent().putExtras(bundle)

                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please fill out the field", Toast.LENGTH_LONG).show()
            }
        }
    }
}