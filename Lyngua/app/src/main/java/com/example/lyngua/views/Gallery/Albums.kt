package com.example.lyngua.views.Gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lyngua.R
import com.example.lyngua.controllers.GalleryController
import com.example.lyngua.models.Photos.Album
import com.example.lyngua.views.Gallery.Dialogs.*
import kotlinx.android.synthetic.main.fragment_albums.*

class Albums : Fragment() {

    private lateinit var galleryController: GalleryController
    private lateinit var albumEdited: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryController = GalleryController(requireContext(), requireActivity(), resources.getString(R.string.app_name))

        val albums = galleryController.getAlbums()

        val adapter = AlbumAdapter(albums) { moreButtonCallback(it) }
        val albumGrid = recyclerView_album_grid
        albumGrid.adapter = adapter
        albumGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        albumGrid.addItemDecoration(SpaceItemDecorator(left = 20, top = 20, right = 20, bottom = 20))

        //Create Album Button - Display create album dialog
        button_create_album.setOnClickListener {
            val alertSheet = CreateAlbum()

            alertSheet.setTargetFragment(this, REQUEST_CODE_DIALOG)
            alertSheet.show(parentFragmentManager, "alertSheetCreateAlbum")
        }

        //When live album data changes notify adapter to update data and view
        galleryController.liveAlbumData.observe(viewLifecycleOwner, { albumList ->
            adapter.setData(albumList as MutableList<Album>)
        })
    }

    /*
     * Purpose: Display bottom sheet for album options
     * Input:   albumName   - String for the album name
     * Output:  None
     */
    private fun moreButtonCallback(albumName: String) {
        val bottomSheet = EditAlbum()
        albumEdited = albumName

        bottomSheet.setTargetFragment(this, REQUEST_CODE_BOTTOM_SHEET)
        bottomSheet.show(parentFragmentManager, "bottomSheetEditAlbum")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //If the result is from a bottom sheet check for the album edit choice
        if (requestCode == REQUEST_CODE_BOTTOM_SHEET && resultCode == Activity.RESULT_OK) {
            if (data?.extras?.containsKey("editChoice")!!) {
                val editChoice = data.extras?.getBoolean("editChoice")!!

                //If the choice was to edit album display the edit album dialog
                if (editChoice) {
                    val alertSheet = EditAlbumName(albumEdited)

                    alertSheet.setTargetFragment(this, REQUEST_CODE_DIALOG)
                    alertSheet.show(parentFragmentManager, "alertSheetEditAlbumName")

                //If the choice was to delete album display the delete album dialog
                } else {
                    val alertSheet = DeleteAlbum(albumEdited)

                    alertSheet.setTargetFragment(this, REQUEST_CODE_DIALOG)
                    alertSheet.show(parentFragmentManager, "alertSheetDeleteAlbum")
                }
            }

        //If the result is from a dialog check which dialog returned data
        } else if (requestCode == REQUEST_CODE_DIALOG && resultCode == Activity.RESULT_OK) {
            //If the dialog was for creating an album call the controller's create album function
            if (data?.extras?.containsKey("createAlbum")!!) {
                val albumName = data.extras?.getString("createAlbum")!!

                val response = galleryController.createAlbum(albumName)

                if (response)
                    Toast.makeText(requireContext(), "Successfully created album", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Failed to create album", Toast.LENGTH_SHORT).show()

            //If the dialog was for editing album name call the controller's set name function
            } else if (data.extras?.containsKey("newAlbumName")!!) {
                val albumName = data.extras?.getString("newAlbumName")!!

                val response = galleryController.setAlbumName(albumEdited, albumName)

                if (response)
                    Toast.makeText(context, "Successfully set album name", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "Failed to set album name", Toast.LENGTH_SHORT).show()

            //If the dialog was for deleting album call the controller's delete album function
            } else if (data.extras?.containsKey("deleteAlbum")!!) {
                val delete = data.extras?.getBoolean("deleteAlbum")!!

                if (delete) {
                    val response = galleryController.deleteAlbum(albumEdited)

                    if (response)
                        Toast.makeText(context, "Album deleted", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context, "Failed to delete album", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(context, "Album not deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_BOTTOM_SHEET = 100
        private const val REQUEST_CODE_DIALOG = 101
    }
}