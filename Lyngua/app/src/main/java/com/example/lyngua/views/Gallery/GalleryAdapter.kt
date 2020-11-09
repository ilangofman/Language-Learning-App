package com.example.lyngua.views.Gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class GalleryAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    //Fragments and their names to use for the View Pager
    var fragments = arrayListOf(PhotoLibrary(), Albums())
    var fragmentNames = arrayListOf("Photo Library", "Albums")

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}