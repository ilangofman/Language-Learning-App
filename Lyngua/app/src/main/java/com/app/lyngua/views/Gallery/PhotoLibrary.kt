package com.app.lyngua.views.Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.lyngua.MainNavigationDirections
import com.app.lyngua.R
import com.app.lyngua.controllers.GalleryController
import kotlinx.android.synthetic.main.fragment_photo_library.*


class PhotoLibrary(private val albumName: String = "") : Fragment() {

    private lateinit var galleryController: GalleryController

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

        //Send album name to controller to get photos for specified album
        // If album name is blank then all photos will return
        val adapter = PhotoLibraryAdapter(galleryController.getPhotos(albumName))
        val photoGrid = recyclerView_photo_grid

        //SpannedGridLayoutManager has a bug when recyclerview only has 1 item
        // so in this case just use GridLayoutManager
        if (adapter.itemCount > 1) photoGrid.layoutManager = SpannedGridLayoutManager(3, 0.75F)
        else photoGrid.layoutManager = GridLayoutManager(requireContext(), 2)

        photoGrid.addItemDecoration(SpaceItemDecorator(left = 1, top = 1, right = 1, bottom = 1))
        photoGrid.adapter = adapter

        //If fragment was called from a fragment container then double back for back button press
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        callback.isEnabled = false
        if (parentFragmentManager.fragments.size == 1) {
            callback.isEnabled = true
            button_back.visibility = View.VISIBLE
            button_play.visibility = View.VISIBLE

            button_back.setOnClickListener {
                requireActivity().onBackPressed()
            }
            button_play.setOnClickListener {
                val photoList = galleryController.getPhotos(albumName)
                if (photoList.isNotEmpty()) {
                    val action = MainNavigationDirections.actionGlobalPhotoPractice(photoList.toTypedArray())
                    findNavController().navigate(action)
                }
            }
        }

        button_select.setOnClickListener {
            button_back.visibility = View.GONE
            button_select.visibility = View.GONE
            button_play.visibility = View.GONE
            button_cancel.visibility = View.VISIBLE
            button_delete.visibility = View.VISIBLE
            (photoGrid.adapter as PhotoLibraryAdapter).toggleSelectEnabled()
        }

        button_cancel.setOnClickListener {
            if (parentFragmentManager.fragments.size == 1) {
                button_back.visibility = View.VISIBLE
                button_play.visibility = View.VISIBLE
            }
            button_select.visibility = View.VISIBLE
            button_cancel.visibility = View.GONE
            button_delete.visibility = View.GONE
            (photoGrid.adapter as PhotoLibraryAdapter).toggleSelectEnabled()
        }

        button_delete.setOnClickListener {
            val selectedList = (photoGrid.adapter as PhotoLibraryAdapter).getSelectedList()
            galleryController.deletePhotos(selectedList)
            (photoGrid.adapter as PhotoLibraryAdapter).setData(galleryController.getPhotos(albumName))
            button_cancel.callOnClick()
        }
    }

    override fun onResume() {
        super.onResume()
        (recyclerView_photo_grid.adapter as PhotoLibraryAdapter).setData(galleryController.getPhotos(albumName))
    }
}