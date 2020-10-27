package com.example.lyngua.views.Gallery

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyngua.R
import com.example.lyngua.models.Albums.Photo
import kotlinx.android.synthetic.main.custom_gallery_image.view.*

class PhotoLibraryAdapter(private var photoList: MutableList<Photo>) : RecyclerView.Adapter<PhotoLibraryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_gallery_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImageUri = Uri.parse(photoList[position].uriString)
        holder.itemView.imageView_gallery.setImageURI(currentImageUri)

        //On click - TODO()
        holder.itemView.imageView_gallery.setOnClickListener {
            Log.d("GalleryAdapter", "Image: $currentImageUri clicked")
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}