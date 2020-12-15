package com.example.lyngua.views.Categories

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lyngua.R
import com.example.lyngua.controllers.Session.Companion.WORDS_PER_SESSION
import com.example.lyngua.models.categories.Category
import com.example.lyngua.views.Categories.PracticeDirections
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_OFF
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON_TIMEGOAL
import kotlinx.android.synthetic.main.custom_category_row.view.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>(){



    private var categoryList:MutableList<Category> = arrayListOf()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    /*
    *Purpose: Create the view holder for the recycler view
    * Input: parent, which is the ViewGroup
    *        viewType: int-  0 for the header, and 1 for a regular row
    * Output: MyViewHolder
     */
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

    /*
    *Purpose: Insert the data into each row in the recycler view
    * Input: holder, MyViewHolder - the row in the recycler view
    *        position: the order in the row
    * Output: None
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //if the position is the category data row and not the header of the recycler view
        if(getItemViewType(position) == view_type_normal) {

            val currentCategory = categoryList[position-1]

            holder.itemView.category_name_txt.text = currentCategory.name.toString().capitalize()

            //add the data to the row
            if(currentCategory.goal.goalType == SWITCH_ON){
                val month = currentCategory.goal.time.get(Calendar.MONTH)
                val day = currentCategory.goal.time.get(Calendar.DAY_OF_MONTH)
                holder.itemView.cat_description_txt.text =
                    holder.itemView.resources.getString(R.string.goal_description_words, currentCategory.goal.totalNumWords - currentCategory.goal.numWordsCompleted, DateFormatSymbols().months[month], day)
            }
            else if(currentCategory.goal.goalType == SWITCH_ON_TIMEGOAL){
                val month = currentCategory.goal.time.get(Calendar.MONTH)
                val day = currentCategory.goal.time.get(Calendar.DAY_OF_MONTH)
                holder.itemView.cat_description_txt.text =
                    holder.itemView.resources.getString(R.string.goal_description_time, String.format("%.1f", currentCategory.goal.totalTime - currentCategory.goal.timeSpent).toDouble(), DateFormatSymbols().months[month], day)
            }

            else if(currentCategory.goal.goalType == SWITCH_OFF){
                holder.itemView.cat_description_txt.text =
                    holder.itemView.resources.getString(R.string.goal_empty)
            }
            else {
                holder.itemView.cat_description_txt.text =
                    holder.itemView.resources.getString(R.string.num_words, currentCategory.wordsList.size)
            }

            holder.itemView.category_name_txt.text = currentCategory.name.toString().capitalize()

            //the listener for the update button
            holder.itemView.category_settings_button.setOnClickListener {
                val actionChosen =
                    PracticeDirections.actionPracticeToUpdateCategoryFragment(
                        currentCategory
                    )
                holder.itemView.findNavController().navigate(actionChosen)

            }

            if(currentCategory.goal.goalType == SWITCH_ON) {
                holder.itemView.progress_bar.progress =
                    (currentCategory.goal.numWordsCompleted.toFloat() / currentCategory.goal.totalNumWords.toFloat() * 100).toInt()
                holder.itemView.progress_percentage_txt.text =
                    holder.itemView.resources.getString(R.string.goal_percentage, ((currentCategory.goal.numWordsCompleted.toFloat() / currentCategory.goal.totalNumWords.toFloat()) * 100).toInt())
            }
            else if(currentCategory.goal.goalType == SWITCH_ON_TIMEGOAL) {
                holder.itemView.progress_bar.progress =
                    (currentCategory.goal.timeSpent / currentCategory.goal.totalTime * 100).toInt()
                holder.itemView.progress_percentage_txt.text =
                    holder.itemView.resources.getString(R.string.goal_percentage, (currentCategory.goal.timeSpent / currentCategory.goal.totalTime * 100).toInt())
            }
            else{
                holder.itemView.progress_bar.visibility = View.INVISIBLE
            }

            // the listener for the category selection
            holder.itemView.rowLayout.setOnClickListener {

                if (currentCategory.wordsList.size < WORDS_PER_SESSION) {
                    Toast.makeText(
                        holder.itemView.context,
                        "Not enough words in the category to play, please add ${WORDS_PER_SESSION-currentCategory.wordsList.size} more to play",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Begins the category practice game session
                    val actionChosen =
                        PracticeDirections.actionPracticeToCategoryGame(currentCategory)

                    holder.itemView.findNavController().navigate(actionChosen)

                }
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
