package com.app.lyngua.controllers.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.lyngua.controllers.CategoryController
import com.app.lyngua.models.categories.Category
import com.app.lyngua.models.goals.Goal


class GoalUpdatePublisher() : BroadcastReceiver(){

    private var context: Context? = null

    override fun onReceive(context: Context, intent: Intent?) {

        this.context = context

        //Unbundle the information for the category to receive a notification from the intent
        val bundle = intent?.getBundleExtra("bundle")
        var currentCategory = bundle?.getParcelable<Category>("category")
        var currentGoal = bundle?.getParcelable<Goal>("goal")

        Log.d("UPDATE","GOAL RESET")

        if (currentCategory != null && currentGoal != null) {
            currentGoal.updateGoal()

            //Resets the current progress for the goal
            val categoryController = CategoryController(this.context!!)
            val result = categoryController.updateCategoryGoal(currentCategory.id, currentGoal)
        }
    }
}