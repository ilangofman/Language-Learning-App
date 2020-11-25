package com.example.lyngua.controllers.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.goals.Goal


class GoalUpdatePublisher() : BroadcastReceiver(){

    private var context: Context? = null

    override fun onReceive(context: Context, intent: Intent?) {

        this.context = context

        //Unbundle the information for the category to receive a notification from the intent
        val bundle = intent?.getBundleExtra("bundle")
        var currentCategory = bundle?.getParcelable<Category>("category")
        var currentGoal = bundle?.getParcelable<Goal>("goal")

        Log.d("HELLO","GOAL RESET")

        if (currentCategory != null && currentGoal != null) {
            currentGoal.updateGoal()

            //Resets the current progress for the goal
            val categoryController = CategoryController(this.context!!)
            val result = categoryController.updateCategoryGoal(currentCategory.id, currentGoal)
        }
    }
}