package com.example.lyngua.views.Categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.views.Categories.CategoryListAdapter
import kotlinx.android.synthetic.main.fragment_practice.view.*

class Practice : Fragment() {

    private lateinit var categoryController: CategoryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_practice, container, false)

        categoryController = CategoryController(requireContext())

        // Call the category list adapter to inflate all the data inside the recycler view
        val listAdapter = CategoryListAdapter()
        val categoryRecyclerView = view.categoryRecyclerView
        categoryRecyclerView.adapter = listAdapter

        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        categoryController.readAllData.observe(viewLifecycleOwner, Observer { category ->
            listAdapter.setData(category)
        })

        view.addCategoryButton.setOnClickListener{
            findNavController().navigate(R.id.action_practice_to_addCategory)
        }

        return view
    }



}