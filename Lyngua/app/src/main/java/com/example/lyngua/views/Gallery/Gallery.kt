package com.example.lyngua.views.Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.lyngua.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*


class Gallery : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_gallery, container, false)
        val adapter = GalleryAdapter(requireActivity())
        val viewPager = view.viewPager_gallery

        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(tabLayout_gallery, viewPager_gallery) { tab, position ->
            tab.text = (viewPager_gallery.adapter as GalleryAdapter).fragmentNames[position]
        }.attach()
    }
}