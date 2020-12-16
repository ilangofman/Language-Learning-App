package com.app.lyngua.views.Gallery

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.lyngua.R
import com.app.lyngua.models.photos.Photo
import com.app.lyngua.views.Gallery.Dialogs.InteractiveQuestion
import kotlinx.android.synthetic.main.fragment_photo_practice.*

class PhotoPractice : Fragment() {

    private val args: PhotoPracticeArgs by navArgs()
    private var photoIndex = 0
    private var photoList = listOf<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_practice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoList = args.photoList.toList()
        displayPhoto()
    }

    private fun displayPhoto() {
        val currentPhoto = photoList[photoIndex]

        imageView.setImageURI(Uri.parse(currentPhoto.uriString))

        button_close.setOnClickListener {
            findNavController().popBackStack()
        }

        button_identify.setOnClickListener {
            displayInteractiveQuestion(currentPhoto)
        }

        if (photoIndex+1 == photoList.size) {
            button_next.setImageResource(R.drawable.ic_done_white)
            button_next.setOnClickListener {
                findNavController().popBackStack()
            }
        } else {
            button_next.setImageResource(R.drawable.ic_arrow_forward_white)
            button_next.setOnClickListener {
                photoIndex += 1
                displayPhoto()
            }
        }

        displayInteractiveQuestion(currentPhoto)
    }

    private fun displayInteractiveQuestion(photo: Photo) {
        val bottomSheet = InteractiveQuestion(photo.word, photo.options, photo.correct)
        bottomSheet.setTargetFragment(this, REQUEST_CODE_BOTTOM_SHEET)
        bottomSheet.show(parentFragmentManager, "bottomSheetInteractiveQuestion")
    }

    companion object {
        private const val REQUEST_CODE_BOTTOM_SHEET = 100
    }
}