package com.example.lyngua.views.Categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lyngua.R
import com.example.lyngua.models.categories.Category
import kotlinx.android.synthetic.main.custom_category_row.view.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*

class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>(){



    private var categoryList:MutableList<Category> = arrayListOf()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if(viewType == view_type_normal) {
            MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_category_row, parent, false)
            )
        }else{
            MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_header, parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(getItemViewType(position) == view_type_normal) {
            val currentCategory = categoryList[position-1]
            holder.itemView.cat_description_txt.text =
                "This is # words: ${currentCategory.wordsList.size},"
            holder.itemView.category_name_txt.text = currentCategory.name.toString()
            holder.itemView.category_settings_button.setOnClickListener {
                val actionChosen =
                    PracticeModeDirections.actionPracticeModeToUpdateCategoryFragment(
                        currentCategory
                    )
                holder.itemView.findNavController().navigate(actionChosen)

            }

            holder.itemView.rowLayout.setOnClickListener {
                val actionChosen =
                    PracticeModeDirections.actionPracticeModeToViewWordsFragment(
                        currentCategory
                    )
                holder.itemView.findNavController().navigate(actionChosen)

            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size +1
    }

    fun setData(categoryList: List<Category>){
        this.categoryList = categoryList as MutableList<Category>
        notifyDataSetChanged()
    }

    companion object ViewTypes{
        const val  view_type_header: Int = 1
        const val  view_type_normal: Int = 2
        val  view_type_footer: Int = 3
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ViewTypes.view_type_header
        } else ViewTypes.view_type_normal
    }


}