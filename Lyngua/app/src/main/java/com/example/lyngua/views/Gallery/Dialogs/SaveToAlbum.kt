package com.example.lyngua.views.Gallery.Dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.lyngua.R
import com.example.lyngua.controllers.GalleryController
import kotlinx.android.synthetic.main.fragment_save_to_album.*


class SaveToAlbum : DialogFragment() {

    private lateinit var galleryController: GalleryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_to_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryController = GalleryController(
            requireContext(), requireActivity(), resources.getString(
                R.string.app_name
            )
        )

        val albumList = galleryController.getAlbums()

        //Populate scrollview with clickable album names
        albumList.forEach { album ->
            val albumView = TextView(requireContext())

            albumView.text = album.name
            albumView.textSize = 18F
            albumView.setTextColor(Color.BLACK)
            albumView.typeface = Typeface.DEFAULT_BOLD
            albumView.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            albumView.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, resources.displayMetrics).toInt()

            //On click - Send back chosen album name
            albumView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("albumName", album.name)

                val intent = Intent().putExtras(bundle)

                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

                dismiss()
            }

            albumListLayout.addView(albumView)
        }

        //Create button - Send back true boolean to parent representing choice to create new album
        button_create_album.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("createNewAlbum", true)

            val intent = Intent().putExtras(bundle)

            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

            dismiss()
        }
    }
}