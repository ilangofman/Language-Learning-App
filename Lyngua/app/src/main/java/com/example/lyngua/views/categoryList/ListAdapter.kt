package com.example.lyngua.views.categoryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lyngua.R
import com.example.lyngua.models.categories.Category
import kotlinx.android.synthetic.main.custom_category_row.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>(){

    private var categoryList = emptyList<Category>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_category_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        holder.itemView.cat_description_txt.text= "This is category ${currentCategory.id}, words left: ${currentCategory.numWords}."
//        holder.itemView.category_id_txt.text = currentCategory.id.toString()
        holder.itemView.category_name_txt.text = currentCategory.name.toString()
//        holder.itemView.number_words_left_txt.text = currentCategory.numWords.toString()
        holder.itemView.rowLayout.setOnClickListener {
            val actionChosen = PracticeModeDirections.actionPracticeModeToUpdateCategoryFragment(currentCategory)
            holder.itemView.findNavController().navigate(actionChosen)

        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setData(categoryList: List<Category> ){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

}