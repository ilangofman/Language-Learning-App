package com.app.lyngua.views.Categories


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.app.lyngua.R
import com.app.lyngua.controllers.CategoryController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_update_category.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*

class UpdateCategory : Fragment() {


    private val args by navArgs<UpdateCategoryArgs>()
    private lateinit var updateCategoryAdapter: UpdateCategoryAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var categoryController: CategoryController

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

        categoryController = CategoryController(requireContext())

        button_add_word.setOnClickListener{
            val alertSheet = AddWord(args)

            alertSheet.setTargetFragment(this, REQUESTCODE)
            alertSheet.show(parentFragmentManager, "alertSheetAddWord")
        }

        TabLayoutMediator(tabLayout_goal, viewPager_goal) { tab, position ->
            tab.text = (viewPager_goal.adapter as UpdateCategoryAdapter).fragmentNames[position]
        }.attach()
    }

    companion object SwitchType{
        const val  SWITCH_ON_TIMEGOAL = 2
        const val  SWITCH_ON: Int = 1
        const val  SWITCH_OFF: Int = 0
        const val  REQUESTCODE = 100
    }

}