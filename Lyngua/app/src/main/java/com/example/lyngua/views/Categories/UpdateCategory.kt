package com.example.lyngua.views.Categories


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.controllers.notifications.AlarmService
import com.example.lyngua.models.goals.Goal
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_update_category.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*
import java.util.*
import kotlin.collections.ArrayList

class UpdateCategory : Fragment() {


    private val args by navArgs<UpdateCategoryArgs>()

    private lateinit var updateCategoryAdapter: UpdateCategoryAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update_category, container, false)
        val adapter = UpdateCategoryAdapter(requireActivity(), args)
        val viewPager = view.viewPager_goal

        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(tabLayout_goal, viewPager_goal) { tab, position ->
            tab.text = (viewPager_goal.adapter as UpdateCategoryAdapter).fragmentNames[position]
        }.attach()
    }

}