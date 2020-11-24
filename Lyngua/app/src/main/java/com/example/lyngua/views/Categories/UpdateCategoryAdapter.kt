package com.example.lyngua.views.Categories

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lyngua.views.Categories.goals.TimeIntervalGoal
import com.example.lyngua.views.Categories.goals.WordIntervalGoal

class UpdateCategoryAdapter(activity: FragmentActivity, args: UpdateCategoryArgs) : FragmentStateAdapter(activity) {

    //Fragments and their names to use for the View Pager
    var fragments = arrayListOf(WordIntervalGoal(args), TimeIntervalGoal(args))
    var fragmentNames = arrayListOf("Word Goal", "Time Goal")

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
