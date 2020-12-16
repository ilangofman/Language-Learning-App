package com.app.lyngua.views.Gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.app.lyngua.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_gallery.*


class Gallery : Fragment() {

    private val args: GalleryArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GalleryAdapter(requireActivity())

        viewPager_gallery.adapter = adapter
        viewPager_gallery.isUserInputEnabled = false
        viewPager_gallery.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        Log.d(this.toString(), "Page = ${args.pageNum}")
        viewPager_gallery.setCurrentItem(args.pageNum, false)

        TabLayoutMediator(tabLayout_gallery, viewPager_gallery) { tab, position ->
            tab.text = (viewPager_gallery.adapter as GalleryAdapter).fragmentNames[position]
        }.attach()
    }
}