package com.example.lyngua.views.Gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import kotlinx.android.synthetic.main.fragment_album_photos.view.*

class AlbumPhotos : Fragment() {

    private val args: AlbumPhotosArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_album_photos, container, false)

        val albumName = args.albumName

        //Replace the current fragment with the Photo Library fragment and pass in
        // the album name to display the photos in the album
        val albumPhotos = PhotoLibrary(albumName)
        val transaction = childFragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_container, albumPhotos)
        transaction.addToBackStack("AlbumPhotos")

        transaction.commit()

        return view
    }

}