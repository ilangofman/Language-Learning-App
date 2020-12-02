package com.example.lyngua.views

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lyngua.MainNavigationDirections
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.controllers.GalleryController
import com.example.lyngua.controllers.UserController
import com.example.lyngua.models.User.User
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_OFF
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON_TIMEGOAL
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.custom_category_row.view.*
import kotlinx.android.synthetic.main.custom_gallery_album.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DateFormatSymbols
import java.util.*


class Home : Fragment() {

    private lateinit var navBar: BottomNavigationView
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

        button_photos_all.setOnClickListener {
            val action = MainNavigationDirections.actionGlobalGallery(0)
            findNavController().navigate(action)
        }

        button_category_all.setOnClickListener {
            navBar.selectedItemId = R.id.practice
        }

        button_album_all.setOnClickListener {
            val action = MainNavigationDirections.actionGlobalGallery(1)
            findNavController().navigate(action)
        }

        button.setOnClickListener {
            navBar.visibility = View.GONE
            findNavController().navigate(R.id.action_home_to_start_navigation)
        }


        initToolbarScroll()

        initPhotos()

        initCategory()

        initAlbum()
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

        if (recents.isEmpty()) return

        textView_photos_empty.visibility = View.GONE

        recents.forEach {
            val image = ImageView(requireContext())
            image.setImageURI(Uri.parse(it.uriString))
            image.scaleType = ImageView.ScaleType.CENTER_CROP

            val card = CardView(requireContext())
            val params = LinearLayout.LayoutParams(125.toPx(), 125.toPx())
            params.marginEnd = 25.toPx()
            card.layoutParams = params
            card.radius = 10.toPx().toFloat()
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
        val category = categoryLive ?: return

        textView_category_empty.visibility = View.GONE
        layout_category.visibility = View.VISIBLE
        layout_category.category_name_txt.text = category.name.capitalize()

        when (category.goal.goalType) {
            SWITCH_ON -> {
                val month = category.goal.time.get(Calendar.MONTH)
                val day = category.goal.time.get(Calendar.DAY_OF_MONTH)
                layout_category.cat_description_txt.text =
                    resources.getString(R.string.goal_description_words, category.goal.totalNumWords - category.goal.numWordsCompleted, DateFormatSymbols().months[month], day)
                layout_category.progress_bar.progress =
                    (category.goal.numWordsCompleted.toFloat() / category.goal.totalNumWords.toFloat() * 100).toInt()
                layout_category.progress_percentage_txt.text =
                    resources.getString(R.string.goal_percentage, ((category.goal.numWordsCompleted.toFloat() / category.goal.totalNumWords.toFloat()) * 100).toInt())
            }
            SWITCH_ON_TIMEGOAL -> {
                val month = category.goal.time.get(Calendar.MONTH)
                val day = category.goal.time.get(Calendar.DAY_OF_MONTH)
                layout_category.cat_description_txt.text =
                    resources.getString(R.string.goal_description_time, category.goal.totalTime - category.goal.timeSpent, DateFormatSymbols().months[month], day)
                layout_category.progress_bar.progress =
                    (category.goal.timeSpent.toFloat() / category.goal.totalTime.toFloat() * 100).toInt()
                layout_category.progress_percentage_txt.text =
                   resources.getString(R.string.goal_percentage, ((category.goal.timeSpent.toFloat() / category.goal.totalTime.toFloat()) * 100).toInt())
            }
            SWITCH_OFF -> {
                layout_category.cat_description_txt.text = resources.getString(R.string.goal_empty)
                layout_category.progress_bar.visibility = View.INVISIBLE
            }
            else -> {
                layout_category.cat_description_txt.text = resources.getString(R.string.num_words, category.wordsList.size)
                layout_category.progress_bar.visibility = View.INVISIBLE
            }
        }

        layout_category.setOnClickListener {
            val actionChosen = HomeDirections.actionHomeToCategoryGame(category)
            findNavController().navigate(actionChosen)
        }
    }

    private fun initAlbum() {
        val recent = galleryController.getRecentAlbum() ?: return

        //layout_album.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, layout_album.width/3*4)

        if (recent.coverPhoto != null)
            layout_album.imageView_gallery.setImageURI(Uri.parse(recent.coverPhoto))
        else
            layout_album.imageView_gallery.setImageResource(R.drawable.empty_album_shape)

        layout_album.textView_album_title.text = recent.name
        layout_album.button_album_more.visibility = View.GONE
        layout_album.button_album_play.visibility = View.GONE

        layout_album.setOnClickListener {
            val actionChosen = MainNavigationDirections.actionGlobalAlbumPhotos(recent.name)
            findNavController().navigate(actionChosen)
        }
    }

    private fun Int.toPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
