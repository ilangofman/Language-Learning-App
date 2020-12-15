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

    private var selectEnabled: Boolean = false
    private var selectedPhotos: MutableSet<Photo> = mutableSetOf()

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

            if (selectEnabled) {
                holder.itemView.view_dim.visibility = View.VISIBLE
                holder.itemView.imageView_select.visibility = View.VISIBLE
                selectedPhotos.add(currentPhoto)
                Log.d("GalleryAdapter", "Selected Photos = $selectedPhotos")
            } else {
                val action = MainNavigationDirections.actionGlobalPhotoPractice(arrayOf(currentPhoto))
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    /*
     * Purpose: Update the data and the recyclerview
     * Input:   photoList   - List of photo objects
     * Output:  None
     */
    fun setData(photoList: MutableList<Photo>) {
        this.photoList = photoList
        notifyDataSetChanged()
    }

    /*
     * Purpose: Toggle the selectEnabled variable
     * Input:   None
     * Output:  None
     */
    fun toggleSelectEnabled() {
        selectEnabled = !selectEnabled
    }

    /*
     * Purpose: Get the selectedList
     * Input:   None
     * Output:  List of selected photos
     */
    fun getSelectedList(): List<Photo> {
        return selectedPhotos.toList()
    }
}