package com.example.lyngua.views.Gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lyngua.R
import com.example.lyngua.controllers.GalleryController
import com.example.lyngua.views.SpaceItemDecorator
import kotlinx.android.synthetic.main.fragment_photo_library.*


class PhotoLibrary(private val albumName: String = "") : Fragment() {

    private lateinit var galleryController: GalleryController
    private var layoutManager = SpannedGridLayoutManager(3, 0.75F)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryController = GalleryController(requireContext(), requireActivity(), resources.getString(R.string.app_name))

        //If fragment was called from a fragment container then double back for back button press
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        button_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        callback.isEnabled = false
        if (parentFragmentManager.fragments.size == 1) {
            callback.isEnabled = true
            button_back.visibility = View.VISIBLE
        }

        //Send album name to controller to get photos for specified album
        // If album name is blank then all photos will return
        val adapter = PhotoLibraryAdapter(galleryController.getPhotos(albumName))
        val photoGrid = recyclerView_photo_grid

        //SpannedGridLayoutManager has a bug when recyclerview only has 1 item
        // so in this case just use GridLayoutManager
        if (adapter.itemCount > 1) photoGrid.layoutManager = layoutManager
        else photoGrid.layoutManager = GridLayoutManager(requireContext(), 2)

        photoGrid.addItemDecoration(SpaceItemDecorator(left = 1, top = 1, right = 1, bottom = 1))
        photoGrid.adapter = adapter
    }
}