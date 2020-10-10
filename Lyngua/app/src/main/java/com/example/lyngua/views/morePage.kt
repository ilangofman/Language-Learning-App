package com.example.lyngua.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lyngua.R

/**
 * A simple [Fragment] subclass.
 * Use the [morePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class morePage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_page, container, false)
    }
}