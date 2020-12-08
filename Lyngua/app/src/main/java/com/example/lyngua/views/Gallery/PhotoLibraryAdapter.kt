package com.example.lyngua.views.Gallery

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lyngua.MainNavigationDirections
import com.example.lyngua.R
import com.example.lyngua.models.photos.Photo
import kotlinx.android.synthetic.main.custom_gallery_image.view.*

class PhotoLibraryAdapter(private var photoList: MutableList<Photo>) : RecyclerView.Adapter<PhotoLibraryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_gallery_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPhoto = photoList[position]
        holder.itemView.imageView_gallery.setImageURI(Uri.parse(currentPhoto.uriString))

        //On click - TODO()
        holder.itemView.imageView_gallery.setOnClickListener {
            Log.d("GalleryAdapter", "Image: ${currentPhoto.word} clicked")

            Log.d("GalleryAdapter", "\nWord = ${currentPhoto.word}\nOptions = ${currentPhoto.options}")

            val action = MainNavigationDirections.actionGlobalPhotoPractice(arrayOf(currentPhoto))
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}