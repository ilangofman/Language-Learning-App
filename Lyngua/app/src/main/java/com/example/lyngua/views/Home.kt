package com.example.lyngua.views

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.controllers.GalleryController
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.User.User
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.custom_category_row.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DateFormatSymbols
import java.util.*


class Home : Fragment() {

    lateinit var navBar: BottomNavigationView
    private val userController: UserController = UserController()
    private lateinit var galleryController: GalleryController
    private lateinit var categoryController: CategoryController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryController = GalleryController(
            requireContext(), requireActivity(), resources.getString(
                R.string.app_name
            )
        )
        categoryController = CategoryController(requireContext())

        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.visibility = View.VISIBLE

        val user: User? = userController.readUserInfo(requireContext())
        if (user == null) {
            navBar.visibility = View.GONE
            findNavController().navigate(R.id.action_home_to_start_navigation)
        } else {
            textView_name.text = user.firstName
            if (user.profilePicture != null) {
                Log.d(this.toString(), "Uri = ${user.profilePicture}")
                imageView_profile.setImageURI(Uri.parse(user.profilePicture))
            }
        }

        imageView_profile.setOnClickListener {
            navBar.selectedItemId = R.id.more
        }

        button_category_all.setOnClickListener {
            navBar.selectedItemId = R.id.practice
        }

        button.setOnClickListener {
            navBar.visibility = View.GONE
            findNavController().navigate(R.id.action_home_to_start_navigation)
        }


        initToolbarScroll()

//        initPhotos()

        initCategory()
    }

    private fun initToolbarScroll() {
        var titleShowing = true
        var scrollRange = -1
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbarLayout.title = "Home"
                titleShowing = true
            } else if (titleShowing) {
                collapsingToolbarLayout.title = " "
                titleShowing = false
            }
        })
    }

    private fun initPhotos() {
        val recents = galleryController.getRecentPhotos(5)

        recents.forEach {
            val image = ImageView(requireContext())
            image.setImageURI(Uri.parse(it.uriString))
            image.scaleType = ImageView.ScaleType.CENTER_CROP

            val card = CardView(requireContext())
            card.layoutParams = ViewGroup.LayoutParams(300, 300)
            card.radius = 10F
            card.addView(
                image, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            linearLayout_recent_photos.addView(card)
        }
    }

    private fun initCategory() {
        val categoryLive = categoryController.getRecentCategory()
        val category = categoryLive

        Log.d(this.toString(), "Category = ${category.toString()}")

        if (category != null) {
            layout_category.visibility = View.VISIBLE
            layout_category.category_name_txt.text = category.name.capitalize(Locale.getDefault())

            when (category.goal.goalType) {
                0 -> {
                    val month = category.goal.time.get(Calendar.MONTH)
                    val day = category.goal.time.get(Calendar.DAY_OF_MONTH)
                    layout_category.cat_description_txt.text =
                        "${category.goal.totalNumWords - category.goal.numWordsCompleted} words to complete by ${DateFormatSymbols().months[month]} $day"
                }
                -1 -> {
                    layout_category.cat_description_txt.text = "You have no goals for this category"
                }
                else -> {
                    layout_category.cat_description_txt.text = "This is # words: ${category.wordsList.size},"
                }
            }

            layout_category.setOnClickListener {
                val actionChosen = HomeDirections.actionHomeToCategoryGame(category)
                findNavController().navigate(actionChosen)
            }
        }
    }
}
